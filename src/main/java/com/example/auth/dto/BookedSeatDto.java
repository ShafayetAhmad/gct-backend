package com.example.auth.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.auth.model.BookingStatus;
import com.example.auth.model.DiscountType;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BookedSeatDto {
    private String seatLocation;
    private BigDecimal price;
    private DiscountType discountType;
} 