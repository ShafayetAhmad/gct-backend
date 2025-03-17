package com.example.auth.dto;

import com.example.auth.model.ReviewStatus;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String userFullName;
    private String playTitle;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private ReviewStatus status;
}