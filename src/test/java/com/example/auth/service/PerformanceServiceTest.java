package com.example.auth.service;

import com.example.auth.dto.PerformanceRequest;
import com.example.auth.dto.PerformanceResponse;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.model.Performance;
import com.example.auth.model.Play;
import com.example.auth.repository.PerformanceRepository;
import com.example.auth.repository.PlayRepository;
import com.example.auth.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceTest {

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private PlayRepository playRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private PerformanceService performanceService;

    private Play play;
    private Performance performance;
    private PerformanceRequest request;

    @BeforeEach
    void setUp() {
        play = new Play();
        play.setId(1L);
        play.setTitle("Test Play");

        performance = new Performance();
        performance.setId(1L);
        performance.setPlay(play);
        performance.setDateTime(LocalDateTime.now().plusDays(1));
        performance.setBasePrice(BigDecimal.valueOf(100));

        request = new PerformanceRequest();
        request.setPlayId(1L);
        request.setDateTime(LocalDateTime.now().plusDays(1));
        request.setBasePrice(BigDecimal.valueOf(100));
    }

    @Test
    void createPerformance_Success() {
        when(playRepository.findById(1L)).thenReturn(Optional.of(play));
        when(performanceRepository.save(any(Performance.class))).thenReturn(performance);
        when(seatRepository.countByPerformanceIdAndIsBookedFalse(1L)).thenReturn(100);

        PerformanceResponse response = performanceService.createPerformance(request);

        assertNotNull(response);
        assertEquals(performance.getId(), response.getId());
        assertEquals(play.getTitle(), response.getPlayTitle());
        verify(seatRepository).saveAll(anyList());
    }

    @Test
    void createPerformance_PlayNotFound() {
        when(playRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> performanceService.createPerformance(request));
    }
}