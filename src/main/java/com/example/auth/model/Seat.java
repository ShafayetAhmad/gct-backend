package com.example.auth.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_number", nullable = false)
    private String row;

    @Column(name = "seat_number", nullable = false)
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatBand band;

    @ManyToOne
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @Column(name = "is_booked", nullable = false)
    private Boolean isBooked = false;
}