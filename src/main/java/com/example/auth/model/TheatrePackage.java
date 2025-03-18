package com.example.auth.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "theatre_packages")
@Data
public class TheatrePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer playsBooked = 0;
    private Integer freeTicketsEarned = 0;
    private Boolean isActive = true;
}