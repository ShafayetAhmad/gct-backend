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
    private Long playId;
    private String playTitle;
    private Long userId;
    private String userFullName;
    private Integer rating;
    private String comment;
    private ReviewStatus status;
    private LocalDateTime createdAt;
}