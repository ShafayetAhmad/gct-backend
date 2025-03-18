package com.example.auth.dto;

import com.example.auth.model.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Payment response details")
public class PaymentResponse {
    @Schema(description = "Transaction ID", example = "TX-A1B2C3D4")
    private String transactionId;

    @Schema(description = "Booking ID", example = "1")
    private Long bookingId;

    @Schema(description = "Payment amount", example = "100.00")
    private BigDecimal amount;

    @Schema(description = "Payment status", example = "COMPLETED")
    private PaymentStatus status;

    @Schema(description = "Payment timestamp", example = "2024-03-18T10:30:00")
    private LocalDateTime paymentTime;
}