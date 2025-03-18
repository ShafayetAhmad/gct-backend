package com.example.auth.dto;

import lombok.Data;
import java.util.List;

@Data
public class SeatSelectionRequest {
    private Long seatId;
    private Long performanceId;
    private String discountType;
    private List<SeatBookingDto> seats;
}
