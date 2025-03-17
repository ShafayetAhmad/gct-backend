package com.example.auth.controller;

import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.ReviewRequest;
import com.example.auth.dto.ReviewResponse;
import com.example.auth.model.ReviewStatus;
import com.example.auth.model.User;
import com.example.auth.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "Review management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Create review", description = "Create a new review for a play")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review created successfully", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid review details"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Play not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user,
            @Parameter(description = "Review details") @Valid @RequestBody ReviewRequest request) {
        ReviewResponse review = reviewService.createReview(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(review));
    }

    @Operation(summary = "Get play reviews", description = "Get all approved reviews for a play")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reviews retrieved successfully", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Play not found")
    })
    @GetMapping("/play/{playId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getPlayReviews(
            @Parameter(description = "Play ID") @PathVariable Long playId) {
        List<ReviewResponse> reviews = reviewService.getPlayReviews(playId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @Operation(summary = "Moderate review", description = "Moderate a review (approve/reject)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review moderated successfully", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PatchMapping("/{reviewId}/moderate")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<ReviewResponse>> moderateReview(
            @Parameter(description = "Review ID") @PathVariable Long reviewId,
            @Parameter(description = "New review status") @RequestParam ReviewStatus status) {
        ReviewResponse review = reviewService.moderateReview(reviewId, status);
        return ResponseEntity.ok(ApiResponse.success(review));
    }
}