package com.example.auth.model;

public enum DiscountType {
    NONE(1.0),
    CHILD(0.5),
    SENIOR(0.7),
    SOCIAL_CLUB(0.8);

    private final double multiplier;

    DiscountType(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}