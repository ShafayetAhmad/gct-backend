package com.example.auth.controller;

import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.PaymentRequest;
import com.example.auth.exception.PaymentException;
import com.example.auth.model.Payment;
import com.example.auth.service.PaymentService;

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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment processing APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Process payment", description = "Process payment for a booking")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment processed successfully", content = @Content(schema = @Schema(implementation = Payment.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payment details"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<Payment>> processPayment(
            @Parameter(description = "Payment details") @Valid @RequestBody PaymentRequest request) {
        try {
            Payment payment = paymentService.processPayment(request);
            return ResponseEntity.ok(ApiResponse.success(payment));
        } catch (PaymentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @Operation(summary = "Refund payment", description = "Refund payment for a booking")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment refunded successfully", content = @Content(schema = @Schema(implementation = Payment.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PostMapping("/{bookingId}/refund")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<ApiResponse<Payment>> refundPayment(
            @Parameter(description = "Booking ID") @PathVariable Long bookingId) {
        Payment payment = paymentService.refundPayment(bookingId);
        return ResponseEntity.ok(ApiResponse.success(payment));
    }
}