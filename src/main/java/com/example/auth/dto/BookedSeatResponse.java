package com.example.auth.dto;

import com.example.auth.model.DiscountType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookedSeatResponse {
    private String seatLocation;
    private String band;
    private DiscountType discountType;
    private BigDecimal price;
}