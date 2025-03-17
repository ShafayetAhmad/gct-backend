package com.example.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth.model.Performance;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    List<Performance> findByPlayIdAndDateTimeAfter(Long playId, LocalDateTime dateTime);

    List<Performance> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
}