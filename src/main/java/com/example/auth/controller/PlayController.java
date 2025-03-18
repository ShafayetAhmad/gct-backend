package com.example.auth.controller;

import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.PlayRequest;
import com.example.auth.dto.PlayResponse;
import com.example.auth.service.PlayService;
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
@RequestMapping("/api/plays")
@RequiredArgsConstructor
@Tag(name = "Plays", description = "Play management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class PlayController {
    private final PlayService playService;

    @Operation(summary = "Create new play", description = "Create a new play (Admin/Staff only)")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<PlayResponse>> createPlay(@Valid @RequestBody PlayRequest request) {
        return ResponseEntity.ok(ApiResponse.success(playService.createPlay(request)));
    }

    @Operation(summary = "Get all plays", description = "Get list of all plays")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PlayResponse>>> getAllPlays() {
        return ResponseEntity.ok(ApiResponse.success(playService.getAllPlays()));
    }

    @Operation(summary = "Get play by ID", description = "Get play details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlayResponse>> getPlay(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(playService.getPlay(id)));
    }

    @Operation(summary = "Update play", description = "Update play details (Admin/Staff only)")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<PlayResponse>> updatePlay(
            @PathVariable Long id,
            @Valid @RequestBody PlayRequest request) {
        return ResponseEntity.ok(ApiResponse.success(playService.updatePlay(id, request)));
    }
}