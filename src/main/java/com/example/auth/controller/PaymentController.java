package com.example.auth.controller;

import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.PaymentRequest;
import com.example.auth.dto.PaymentResponse;
import com.example.auth.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Process payment", description = "Process payment for a booking", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment processed successfully", content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payment details"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PostMapping("/process")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.processPayment(request)));
    }

    @Operation(summary = "Refund payment", description = "Refund payment for a booking (Admin/Staff only)", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment refunded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/{bookingId}/refund")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<PaymentResponse>> refundPayment(
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.refundPayment(bookingId)));
    }
}