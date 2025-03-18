package com.example.auth.repository;

import com.example.auth.model.TheatrePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TheatrePackageRepository extends JpaRepository<TheatrePackage, Long> {
    @Query("SELECT tp FROM TheatrePackage tp WHERE tp.user.id = :userId AND tp.isActive = true")
    Optional<TheatrePackage> findActivePackageByUserId(Long userId);
}