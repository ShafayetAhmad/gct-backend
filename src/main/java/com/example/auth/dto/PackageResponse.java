package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Theatre package details")
public class PackageResponse {
    @Schema(description = "Package ID", example = "1")
    private Long id;

    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "Number of plays booked", example = "3")
    private int playsBooked;

    @Schema(description = "Number of free tickets earned", example = "1")
    private int freeTicketsEarned;

    @Schema(description = "Whether the package is active", example = "true")
    private boolean isActive;
}