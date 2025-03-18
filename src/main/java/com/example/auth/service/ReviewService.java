package com.example.auth.service;

import com.example.auth.dto.ReviewRequest;
import com.example.auth.dto.ReviewResponse;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.model.*;
import com.example.auth.repository.PlayRepository;
import com.example.auth.repository.ReviewRepository;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlayRepository playRepository;
    private final UserRepository userRepository;

    public ReviewResponse submitReview(Long userId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Play play = playRepository.findById(request.getPlayId())
                .orElseThrow(() -> new ResourceNotFoundException("Play not found"));

        Review review = new Review();
        review.setUser(user);
        review.setPlay(play);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setStatus(ReviewStatus.PENDING);

        return mapToReviewResponse(reviewRepository.save(review));
    }

    public List<ReviewResponse> getPlayReviews(Long playId) {
        return reviewRepository.findByPlayIdAndStatus(playId, ReviewStatus.APPROVED)
                .stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse moderateReview(Long reviewId, ReviewStatus status) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setStatus(status);
        return mapToReviewResponse(reviewRepository.save(review));
    }

    @Transactional
    public ReviewResponse createReview(Long userId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = new Review();
        review.setUser(user);
        review.setPlay(playRepository.findById(request.getPlayId())
                .orElseThrow(() -> new ResourceNotFoundException("Play not found")));
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStatus(ReviewStatus.PENDING);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        return mapToReviewResponse(savedReview);
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .playId(review.getPlay().getId())
                .playTitle(review.getPlay().getTitle())
                .userId(review.getUser().getId())
                .userFullName(review.getUser().getFullName())
                .rating(review.getRating())
                .comment(review.getComment())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .build();
    }
}