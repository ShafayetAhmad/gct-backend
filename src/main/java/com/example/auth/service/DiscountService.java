package com.example.auth.service;

import com.example.auth.model.DiscountType;
import com.example.auth.model.Performance;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;

@Service
public class DiscountService {

    public BigDecimal calculateDiscount(Performance performance, DiscountType discountType, int numberOfTickets) {
        BigDecimal discount = BigDecimal.ONE;
        LocalDateTime now = LocalDateTime.now();

        // Base discount from DiscountType
        discount = discount.multiply(BigDecimal.valueOf(discountType.getMultiplier()));

        // Weekday Special (10% off Monday-Thursday)
        DayOfWeek day = performance.getDateTime().getDayOfWeek();
        if (day.getValue() >= DayOfWeek.MONDAY.getValue() &&
                day.getValue() <= DayOfWeek.THURSDAY.getValue()) {
            discount = discount.multiply(BigDecimal.valueOf(0.9));
        }

        // Last Hour Discount (10% off)
        long minutesUntilShow = ChronoUnit.MINUTES.between(now, performance.getDateTime());
        if (minutesUntilShow <= 60 && minutesUntilShow > 0) {
            discount = discount.multiply(BigDecimal.valueOf(0.9));
        }

        // Social Club Group Discount (additional 5% for 20+ tickets)
        if (discountType == DiscountType.SOCIAL_CLUB && numberOfTickets >= 20) {
            discount = discount.multiply(BigDecimal.valueOf(0.95));
        }

        return discount;
    }
}