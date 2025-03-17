package com.example.auth.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class MockVisaCheckService {

    public boolean processPayment(String cardNumber, String expiryDate, String cvv, BigDecimal amount) {
        log.info("Processing mock payment for amount: {}", amount);

        // Simple mock validation
        boolean isValid = cardNumber.length() == 16
                && cardNumber.startsWith("4") // VISA cards start with 4
                && validateExpiryDate(expiryDate)
                && cvv.length() == 3;

        // Simulate processing time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Mock decline for specific amounts or card numbers
        if (amount.compareTo(new BigDecimal("10000")) > 0 || cardNumber.endsWith("0000")) {
            log.info("Payment declined");
            return false;
        }

        log.info("Payment approved");
        return isValid;
    }

    private boolean validateExpiryDate(String expiryDate) {
        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);

            LocalDate expiry = LocalDate.of(2000 + year, month, 1);
            return expiry.isAfter(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }
}