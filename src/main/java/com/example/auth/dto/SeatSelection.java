package com.example.auth.dto;

import com.example.auth.model.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatSelection {
    private Long seatId;
    private DiscountType discountType;
}