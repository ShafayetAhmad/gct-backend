package com.example.auth.dto;

import com.example.auth.model.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Seat booking details")
public class SeatBookingDto {
    @Schema(description = "Seat ID", example = "1")
    @NotNull
    private Long seatId;

    @Schema(description = "Discount type for the seat", example = "NONE")
    private DiscountType discountType;
}