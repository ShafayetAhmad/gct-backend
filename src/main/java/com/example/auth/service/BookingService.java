package com.example.auth.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.auth.dto.BookedSeatDto;
import com.example.auth.dto.BookedSeatResponse;
import com.example.auth.dto.BookingRequest;
import com.example.auth.dto.BookingResponse;
import com.example.auth.dto.SeatBookingDto;
import com.example.auth.dto.SeatSelectionRequest;
import com.example.auth.exception.BookingException;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.exception.UnauthorizedException;
import com.example.auth.exception.ValidationException;
import com.example.auth.model.*;
import com.example.auth.repository.BookingRepository;
import com.example.auth.repository.PerformanceRepository;
import com.example.auth.repository.SeatRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.repository.TheatrePackageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PerformanceRepository performanceRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final DiscountService discountService;
    private final TheatrePackageRepository theatrePackageRepository;
    private final TheatrePackageService packageService;

    public BookingResponse createBooking(Long userId, BookingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        // Validate seats and calculate total price
        List<BookedSeat> bookedSeats = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (SeatBookingDto seatRequest : request.getSeats()) {
            Seat seat = seatRepository.findById(seatRequest.getSeatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

            if (seat.getIsBooked()) {
                throw new ValidationException("Seat " + seat.getRow() + seat.getNumber() + " is already booked");
            }

            BigDecimal seatPrice = calculateSeatPrice(seat, performance, seatRequest.getDiscountType());
            totalPrice = totalPrice.add(seatPrice);

            BookedSeat bookedSeat = new BookedSeat();
            bookedSeat.setSeat(seat);
            bookedSeat.setPrice(seatPrice);
            bookedSeat.setDiscountType(seatRequest.getDiscountType());
            bookedSeats.add(bookedSeat);

            seat.setIsBooked(true);
            seatRepository.save(seat);
        }

        // Apply package discount if applicable
        if (packageService.isEligibleForFreeTicket(userId)) {
            totalPrice = applyPackageDiscount(totalPrice, bookedSeats);
            packageService.useFreeTicket(userId);
        }

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setPerformance(performance);
        booking.setBookedSeats(bookedSeats);
        booking.setTotalPrice(totalPrice);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);

        bookedSeats.forEach(bs -> bs.setBooking(booking));

        Booking savedBooking = bookingRepository.save(booking);
        return mapToBookingResponse(savedBooking);
    }

    private void validatePerformanceAvailability(Performance performance) {
        if (performance.isCancelled() || performance.getDateTime().isBefore(LocalDateTime.now())) {
            throw new BookingException("Performance is not available for booking");
        }
    }

    private void validateSeatAvailability(Performance performance, Seat seat) {
        boolean isBooked = performance.getBookings().stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .flatMap(b -> b.getBookedSeats().stream())
                .anyMatch(bs -> bs.getSeat().equals(seat));

        if (isBooked) {
            throw new BookingException("Seat is already booked");
        }
    }

    private BigDecimal calculateSeatPrice(Seat seat, Performance performance, DiscountType discountType) {
        BigDecimal basePrice = performance.getBasePrice();
        BigDecimal bandMultiplier = BigDecimal.valueOf(seat.getBand().getPriceMultiplier());
        BigDecimal discountMultiplier = BigDecimal.valueOf(discountType.getMultiplier());

        return basePrice.multiply(bandMultiplier).multiply(discountMultiplier);
    }

    private BigDecimal applyPackageDiscount(BigDecimal totalPrice, List<BookedSeat> bookedSeats) {
        // Apply the highest priced seat as free
        return bookedSeats.stream()
                .map(BookedSeat::getPrice)
                .max(BigDecimal::compareTo)
                .map(freeAmount -> totalPrice.subtract(freeAmount))
                .orElse(totalPrice);
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setPerformanceId(booking.getPerformance().getId());
        response.setPerformanceTitle(booking.getPerformance().getTitle());
        response.setPerformanceDateTime(booking.getPerformance().getDateTime());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus());
        response.setBookingTime(booking.getBookingTime());

        List<BookedSeatResponse> seatResponses = booking.getBookedSeats().stream()
                .map(this::mapToBookedSeatResponse)
                .collect(Collectors.toList());
        response.setSeats(seatResponses);

        return response;
    }

    private BookedSeatResponse mapToBookedSeatResponse(BookedSeat bookedSeat) {
        BookedSeatResponse response = new BookedSeatResponse();
        response.setSeatLocation(bookedSeat.getSeat().getRow() + bookedSeat.getSeat().getNumber());
        response.setBand(bookedSeat.getSeat().getBand().name());
        response.setDiscountType(bookedSeat.getDiscountType());
        response.setPrice(bookedSeat.getPrice());
        return response;
    }

    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse cancelBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to cancel this booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING &&
                booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new ValidationException("Booking cannot be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getBookedSeats().forEach(bs -> {
            bs.getSeat().setIsBooked(false);
            seatRepository.save(bs.getSeat());
        });

        return mapToBookingResponse(bookingRepository.save(booking));
    }
}