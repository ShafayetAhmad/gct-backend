package com.example.auth.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.auth.dto.BookedSeatDto;
import com.example.auth.dto.BookingResponse;
import com.example.auth.dto.SeatBookingDto;
import com.example.auth.dto.SeatSelectionRequest;
import com.example.auth.exception.BookingException;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.model.*;
import com.example.auth.repository.BookingRepository;
import com.example.auth.repository.PerformanceRepository;
import com.example.auth.repository.SeatRepository;
import com.example.auth.repository.UserRepository;

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

    public BookingResponse createBooking(Long userId, SeatSelectionRequest request) {
        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validatePerformanceAvailability(performance);

        List<BookedSeat> bookedSeats = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (SeatBookingDto seatDto : request.getSeats()) {
            Seat seat = seatRepository.findById(seatDto.getSeatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

            validateSeatAvailability(performance, seat);

            BigDecimal seatPrice = calculateSeatPrice(seat, seatDto.getDiscountType());
            totalPrice = totalPrice.add(seatPrice);

            BookedSeat bookedSeat = new BookedSeat();
            bookedSeat.setSeat(seat);
            bookedSeat.setPrice(seatPrice);
            bookedSeat.setDiscountType(seatDto.getDiscountType());
            bookedSeats.add(bookedSeat);
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setPerformance(performance);
        booking.setBookedSeats(bookedSeats);
        booking.setTotalPrice(totalPrice);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        bookedSeats.forEach(bs -> bs.setBooking(booking));

        Booking savedBooking = bookingRepository.save(booking);
        return createBookingResponse(savedBooking);
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

    private BigDecimal calculateSeatPrice(Seat seat, DiscountType discountType) {
        return seat.getBasePrice()
                .multiply(BigDecimal.valueOf(seat.getBand().getPriceMultiplier()))
                .multiply(BigDecimal.valueOf(discountType.getMultiplier()));
    }

    private BookingResponse createBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .playTitle(booking.getPerformance().getPlay().getTitle())
                .performanceDateTime(booking.getPerformance().getDateTime())
                .seats(booking.getBookedSeats().stream()
                        .map(this::createBookedSeatDto)
                        .collect(Collectors.toList()))
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().name())
                .bookingTime(booking.getBookingTime())
                .build();
    }

    private BookedSeatDto createBookedSeatDto(BookedSeat bookedSeat) {
        return BookedSeatDto.builder()
                .seatLocation(bookedSeat.getSeat().getRow() + bookedSeat.getSeat().getNumber())
                .price(bookedSeat.getPrice())
                .discountType(bookedSeat.getDiscountType())
                .build();
    }

    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::createBookingResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse cancelBooking(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId()) &&
                !user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") ||
                        a.getAuthority().equals("ROLE_STAFF"))) {
            throw new BookingException("Not authorized to cancel this booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return createBookingResponse(bookingRepository.save(booking));
    }
}