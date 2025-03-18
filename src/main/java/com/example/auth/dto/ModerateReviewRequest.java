package com.example.auth.dto;

import com.example.auth.model.ReviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModerateReviewRequest {
    @NotNull(message = "Status is required")
    private ReviewStatus status;
}