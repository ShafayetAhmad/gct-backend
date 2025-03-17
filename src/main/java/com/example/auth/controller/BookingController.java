package com.example.auth.controller;

import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.BookingResponse;
import com.example.auth.dto.SeatSelectionRequest;
import com.example.auth.model.User;
import com.example.auth.service.BookingService;

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
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking", description = "Booking management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Create a new booking", description = "Creates a new booking for the authenticated user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking created successfully", content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Performance not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user,
            @Parameter(description = "Booking details") @Valid @RequestBody SeatSelectionRequest request) {
        BookingResponse booking = bookingService.createBooking(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @Operation(summary = "Get user's bookings", description = "Retrieves all bookings for the authenticated user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bookings retrieved successfully", content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/my-bookings")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(
            @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user) {
        List<BookingResponse> bookings = bookingService.getUserBookings(user.getId());
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @Operation(summary = "Cancel booking", description = "Cancels an existing booking")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking cancelled successfully", content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PostMapping("/{bookingId}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId,
            @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user) {
        BookingResponse booking = bookingService.cancelBooking(bookingId, user);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }
}