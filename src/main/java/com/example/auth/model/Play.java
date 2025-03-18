package com.example.auth.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plays")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Play {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @OneToMany(mappedBy = "play", cascade = CascadeType.ALL)
    private List<Performance> performances = new ArrayList<>();

    @OneToMany(mappedBy = "play", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
}