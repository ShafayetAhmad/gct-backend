package com.example.auth.repository;

import com.example.auth.model.Review;
import com.example.auth.model.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPlayIdAndStatus(Long playId, ReviewStatus status);

    List<Review> findByUserId(Long userId);

    List<Review> findByStatus(ReviewStatus status);
}