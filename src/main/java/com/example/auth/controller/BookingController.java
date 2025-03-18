package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.model.User;
import com.example.auth.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class BookingController {
        private final BookingService bookingService;

        @PostMapping
        public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
                        @AuthenticationPrincipal User user,
                        @RequestBody BookingRequest request) {
                return ResponseEntity.ok(ApiResponse.success(
                                bookingService.createBooking(user.getId(), request)));
        }

        @GetMapping("/me")
        public ResponseEntity<ApiResponse<List<BookingResponse>>> getUserBookings(
                        @AuthenticationPrincipal User user) {
                return ResponseEntity.ok(ApiResponse.success(
                                bookingService.getUserBookings(user.getId())));
        }

        @PostMapping("/{id}/cancel")
        public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
                        @AuthenticationPrincipal User user,
                        @PathVariable Long id) {
                return ResponseEntity.ok(ApiResponse.success(
                                bookingService.cancelBooking(user.getId(), id)));
        }
}