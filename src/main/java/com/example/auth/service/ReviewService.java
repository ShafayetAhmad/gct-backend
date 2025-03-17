package com.example.auth.service;

import com.example.auth.model.Review;
import com.example.auth.model.ReviewStatus;
import com.example.auth.model.User;
import com.example.auth.model.Play;
import com.example.auth.repository.ReviewRepository;
import com.example.auth.repository.PlayRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.repository.BookingRepository;
import com.example.auth.dto.ReviewRequest;
import com.example.auth.dto.ReviewResponse;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.exception.ValidationException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlayRepository playRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public ReviewResponse createReview(Long userId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Play play = playRepository.findById(request.getPlayId())
                .orElseThrow(() -> new ResourceNotFoundException("Play not found"));

        validateUserHasBooking(userId, play.getId());

        Review review = new Review();
        review.setUser(user);
        review.setPlay(play);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        return createReviewResponse(savedReview);
    }

    private void validateUserHasBooking(Long userId, Long playId) {
        boolean hasBooking = bookingRepository.findByUserId(userId).stream()
                .anyMatch(booking -> booking.getPerformance().getPlay().getId().equals(playId));

        if (!hasBooking) {
            throw new ValidationException("You can only review plays you have booked");
        }
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ReviewResponse moderateReview(Long reviewId, ReviewStatus newStatus) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setStatus(newStatus);
        Review savedReview = reviewRepository.save(review);
        return createReviewResponse(savedReview);
    }

    public List<ReviewResponse> getPlayReviews(Long playId) {
        return reviewRepository.findByPlayIdAndStatus(playId, ReviewStatus.APPROVED)
                .stream()
                .map(this::createReviewResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse createReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userFullName(review.getUser().getFullName())
                .playTitle(review.getPlay().getTitle())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .status(review.getStatus())
                .build();
    }
}