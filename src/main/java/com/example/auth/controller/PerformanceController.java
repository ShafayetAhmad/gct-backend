package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
@Tag(name = "Performances", description = "Performance management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class PerformanceController {
    private final PerformanceService performanceService;

    @Operation(summary = "Create performance", description = "Create a new performance for a play (Admin/Staff only)")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<PerformanceResponse>> createPerformance(
            @Valid @RequestBody PerformanceRequest request) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.createPerformance(request)));
    }

    @Operation(summary = "Get all performances", description = "Get list of all performances")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PerformanceResponse>>> getAllPerformances() {
        return ResponseEntity.ok(ApiResponse.success(performanceService.getAllPerformances()));
    }

    @Operation(summary = "Get performance details", description = "Get performance details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PerformanceDetailResponse>> getPerformance(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.getPerformanceDetails(id)));
    }

    @Operation(summary = "Get seat map", description = "Get seat map for a performance")
    @GetMapping("/{id}/seats")
    public ResponseEntity<ApiResponse<SeatMapResponse>> getSeatMap(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.getSeatMap(id)));
    }
}