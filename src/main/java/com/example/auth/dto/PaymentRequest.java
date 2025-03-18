package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Schema(description = "Payment request details")
public class PaymentRequest {
    @Schema(description = "Booking ID", example = "1")
    @NotNull
    private Long bookingId;

    @Schema(description = "16-digit card number", example = "4111111111111111")
    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String cardNumber;

    @Schema(description = "Card expiry date (MM/YY)", example = "12/25")
    @NotBlank
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Expiry date must be in MM/YY format")
    private String expiryDate;

    @Schema(description = "3-digit CVV", example = "123")
    @NotBlank
    @Pattern(regexp = "\\d{3}", message = "CVV must be 3 digits")
    private String cvv;
}