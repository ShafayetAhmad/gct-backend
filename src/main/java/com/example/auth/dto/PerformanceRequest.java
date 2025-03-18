package com.example.auth.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PerformanceRequest {
    @NotNull(message = "Play ID is required")
    private Long playId;

    @NotNull(message = "Performance date/time is required")
    @Future(message = "Performance must be in the future")
    private LocalDateTime dateTime;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;
}