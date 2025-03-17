package com.example.auth.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.auth.model.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String playTitle;
    private LocalDateTime performanceDateTime;
    private List<BookedSeatDto> seats;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime bookingTime;
}