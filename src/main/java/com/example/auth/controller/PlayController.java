package com.example.auth.controller;

import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.PlayRequest;
import com.example.auth.dto.PlayResponse;
import com.example.auth.model.User;
import com.example.auth.service.PlayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plays")
@RequiredArgsConstructor
@Tag(name = "Plays", description = "Play management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class PlayController {
    private final PlayService playService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @Operation(summary = "Create a new play", description = "Only ADMIN and STAFF can create plays")
    public ResponseEntity<ApiResponse<PlayResponse>> createPlay(@Valid @RequestBody PlayRequest request) {
        return ResponseEntity.ok(ApiResponse.success(playService.createPlay(request)));
    }

    @GetMapping
    @Operation(summary = "Get all plays", description = "Anyone can view all plays")
    public ResponseEntity<ApiResponse<List<PlayResponse>>> getAllPlays() {
        return ResponseEntity.ok(ApiResponse.success(playService.getAllPlays()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get play by ID", description = "Anyone can view a specific play")
    public ResponseEntity<ApiResponse<PlayResponse>> getPlayById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(playService.getPlayById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    @Operation(summary = "Update play details", description = "Only ADMIN and STAFF can update plays")
    public ResponseEntity<ApiResponse<PlayResponse>> updatePlay(
            @PathVariable Long id,
            @Valid @RequestBody PlayRequest request) {
        return ResponseEntity.ok(ApiResponse.success(playService.updatePlay(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a play", description = "Only ADMIN can delete plays")
    public ResponseEntity<ApiResponse<Void>> deletePlay(@PathVariable Long id) {
        playService.deletePlay(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}