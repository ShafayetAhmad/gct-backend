package com.example.auth.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PerformanceDetailResponse {
    private Long id;
    private PlayResponse play;
    private LocalDateTime dateTime;
    private BigDecimal basePrice;
    private SeatMapResponse seatMap;
}