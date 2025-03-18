package com.example.auth.service;

import com.example.auth.dto.*;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.model.*;
import com.example.auth.repository.PerformanceRepository;
import com.example.auth.repository.PlayRepository;
import com.example.auth.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final PlayRepository playRepository;
    private final SeatRepository seatRepository;

    public PerformanceResponse createPerformance(PerformanceRequest request) {
        Play play = playRepository.findById(request.getPlayId())
                .orElseThrow(() -> new ResourceNotFoundException("Play not found"));

        Performance performance = new Performance();
        performance.setPlay(play);
        performance.setDateTime(request.getDateTime());
        performance.setBasePrice(request.getBasePrice());
        performance.setAvailableSeats(request.getAvailableSeats());

        Performance savedPerformance = performanceRepository.save(performance);

        // Create seats for the performance
        createSeats(savedPerformance);

        return mapToPerformanceResponse(savedPerformance);
    }

    public List<PerformanceResponse> getAllPerformances() {
        return performanceRepository.findAll().stream()
                .map(this::mapToPerformanceResponse)
                .collect(Collectors.toList());
    }

    public PerformanceDetailResponse getPerformanceDetails(Long id) {
        Performance performance = findPerformanceById(id);
        return mapToPerformanceDetailResponse(performance);
    }

    public SeatMapResponse getSeatMap(Long performanceId) {
        Performance performance = findPerformanceById(performanceId);
        List<Seat> seats = seatRepository.findByPerformanceId(performanceId);

        return createSeatMapResponse(seats);
    }

    private void createSeats(Performance performance) {
        List<Seat> seats = new ArrayList<>();

        // Create seats for each band
        // Band A (Front rows: 1-5)
        createSeatsForBand(seats, performance, "1", "5", SeatBand.A);
        // Band B (Middle rows: 6-10)
        createSeatsForBand(seats, performance, "6", "10", SeatBand.B);
        // Band C (Back rows: 11-15)
        createSeatsForBand(seats, performance, "11", "15", SeatBand.C);

        seatRepository.saveAll(seats);
    }

    private void createSeatsForBand(List<Seat> seats, Performance performance,
            String startRow, String endRow, SeatBand band) {
        int seatsPerRow = 20; // Assuming 20 seats per row
        for (int row = Integer.parseInt(startRow); row <= Integer.parseInt(endRow); row++) {
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                Seat seat = new Seat();
                seat.setPerformance(performance);
                seat.setRow(String.valueOf(row));
                seat.setNumber(seatNum);
                seat.setBand(band);
                seat.setIsBooked(false);
                seats.add(seat);
            }
        }
    }

    private Performance findPerformanceById(Long id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));
    }

    private PerformanceResponse mapToPerformanceResponse(Performance performance) {
        return PerformanceResponse.builder()
                .id(performance.getId())
                .playTitle(performance.getPlay().getTitle())
                .dateTime(performance.getDateTime())
                .basePrice(performance.getBasePrice())
                .availableSeats(countAvailableSeats(performance.getId()))
                .build();
    }

    private PerformanceDetailResponse mapToPerformanceDetailResponse(Performance performance) {
        return PerformanceDetailResponse.builder()
                .id(performance.getId())
                .play(PlayResponse.builder()
                        .id(performance.getPlay().getId())
                        .title(performance.getPlay().getTitle())
                        .description(performance.getPlay().getDescription())
                        .build())
                .dateTime(performance.getDateTime())
                .basePrice(performance.getBasePrice())
                .seatMap(getSeatMap(performance.getId()))
                .build();
    }

    private SeatMapResponse createSeatMapResponse(List<Seat> seats) {
        SeatMapResponse response = new SeatMapResponse();

        int maxRow = seats.stream()
                .mapToInt(seat -> Integer.parseInt(seat.getRow()))
                .max()
                .orElse(0);

        int maxSeatNumber = seats.stream()
                .mapToInt(Seat::getNumber)
                .max()
                .orElse(0);

        response.setRows(maxRow);
        response.setSeatsPerRow(maxSeatNumber);

        List<SeatResponse> seatResponses = seats.stream()
                .map(this::mapToSeatResponse)
                .collect(Collectors.toList());

        response.setSeats(seatResponses);
        return response;
    }

    private SeatResponse mapToSeatResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .row(seat.getRow())
                .number(seat.getNumber())
                .band(seat.getBand())
                .isBooked(seat.getIsBooked())
                .build();
    }

    private int countAvailableSeats(Long performanceId) {
        return seatRepository.countByPerformanceIdAndIsBookedFalse(performanceId);
    }
}