package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Booking request details")
public class BookingRequest {
    @Schema(description = "Performance ID", example = "1")
    @NotNull
    private Long performanceId;

    @Schema(description = "List of seats to book", example = "[{\"seatId\": 1, \"discountType\": \"NONE\"}]")
    @NotNull
    private List<SeatBookingDto> seats;
}