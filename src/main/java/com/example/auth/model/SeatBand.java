package com.example.auth.model;

public enum SeatBand {
    A(1.0),
    B(0.8),
    C(0.6);

    private final double priceMultiplier;

    SeatBand(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}