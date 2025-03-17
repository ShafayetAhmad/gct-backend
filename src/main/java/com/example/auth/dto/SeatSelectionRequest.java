package com.example.auth.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatSelectionRequest {
    private Long performanceId;
    private List<SeatBookingDto> seats;
}
