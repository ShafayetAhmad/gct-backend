package com.example.auth.repository;

import com.example.auth.model.Seat;
import com.example.auth.model.SeatBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByBand(SeatBand band);

    Optional<Seat> findByRowAndNumber(String row, Integer number);

    List<Seat> findByPerformanceId(Long performanceId);

    int countByPerformanceIdAndIsBookedFalse(Long performanceId);
}