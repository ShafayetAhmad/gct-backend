package com.example.auth.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PerformanceResponse {
    private Long id;
    private String playTitle;
    private LocalDateTime dateTime;
    private BigDecimal basePrice;
    private Integer availableSeats;
}