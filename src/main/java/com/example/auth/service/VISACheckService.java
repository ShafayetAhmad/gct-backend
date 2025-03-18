package com.example.auth.service;

import com.example.auth.exception.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@Slf4j
public class VISACheckService {

    public boolean processPayment(String cardNumber, String expiryDate, String cvv, BigDecimal amount) {
        log.info("Processing payment of {} with card ending in {}",
                amount, cardNumber.substring(cardNumber.length() - 4));

        // Basic validation
        if (!isValidCardNumber(cardNumber)) {
            throw new PaymentException("Invalid card number");
        }

        if (!isValidExpiryDate(expiryDate)) {
            throw new PaymentException("Invalid expiry date");
        }

        if (!isValidCVV(cvv)) {
            throw new PaymentException("Invalid CVV");
        }

        // In a real implementation, this would integrate with the bank's API
        // For now, simulate successful payment
        return true;
    }

    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber != null &&
                cardNumber.matches("\\d{16}") &&
                luhnCheck(cardNumber);
    }

    private boolean isValidExpiryDate(String expiryDate) {
        return expiryDate != null &&
                expiryDate.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
    }

    private boolean isValidCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    private boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }
}