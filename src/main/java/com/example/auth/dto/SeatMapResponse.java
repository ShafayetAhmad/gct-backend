package com.example.auth.dto;

import lombok.Data;
import java.util.List;

@Data
public class SeatMapResponse {
    private Integer rows;
    private Integer seatsPerRow;
    private List<SeatResponse> seats;
}