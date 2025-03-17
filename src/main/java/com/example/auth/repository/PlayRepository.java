package com.example.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth.model.Play;

import java.util.List;

@Repository
public interface PlayRepository extends JpaRepository<Play, Long> {
    List<Play> findByGenre(String genre);

    List<Play> findByTitleContainingIgnoreCase(String title);
}