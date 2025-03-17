package com.example.auth.dto;

import org.antlr.v4.runtime.misc.NotNull;

import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull
    private String cardNumber;

    @NotNull
    private String expiryDate;

    @NotNull
    private String cvv;

    @NotNull
    private Long bookingId;
}