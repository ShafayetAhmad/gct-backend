package com.example.auth.dto;

import com.example.auth.model.SeatBand;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatResponse {
    private Long id;
    private String row;
    private Integer number;
    private SeatBand band;
    private Boolean isBooked;
}