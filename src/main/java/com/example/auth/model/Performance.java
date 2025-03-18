package com.example.auth.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "performances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "play_id", nullable = false)
    private Play play;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private Integer availableSeats;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @Column(nullable = false)
    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    // Helper method to get play title
    public String getTitle() {
        return play != null ? play.getTitle() : null;
    }

    // Helper method to get play description
    public String getDescription() {
        return play != null ? play.getDescription() : null;
    }
}