package com.example.auth.service;

import com.example.auth.dto.PaymentRequest;
import com.example.auth.dto.PaymentResponse;
import com.example.auth.exception.PaymentException;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.model.*;
import com.example.auth.repository.BookingRepository;
import com.example.auth.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final VISACheckService visaCheckService;

    public PaymentResponse processPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Prevent double payment
        if (paymentRepository.existsByBookingId(booking.getId())) {
            throw new PaymentException("Payment already processed for this booking");
        }

        // Process payment through VISACheck
        boolean paymentSuccessful = visaCheckService.processPayment(
                request.getCardNumber(),
                request.getExpiryDate(),
                request.getCvv(),
                booking.getTotalPrice());

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setTransactionId(generateTransactionId());
        payment.setPaymentTime(LocalDateTime.now());

        if (paymentSuccessful) {
            payment.setStatus(PaymentStatus.SUCCESS);
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            booking.setStatus(BookingStatus.CANCELLED);
            throw new PaymentException("Payment failed");
        }

        Payment savedPayment = paymentRepository.save(payment);
        bookingRepository.save(booking);

        return mapToPaymentResponse(savedPayment);
    }

    public PaymentResponse refundPayment(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new PaymentException("Payment cannot be refunded");
        }

        // Process refund through VISACheck (in real implementation)
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.getBooking().setStatus(BookingStatus.REFUNDED);

        Payment savedPayment = paymentRepository.save(payment);
        bookingRepository.save(payment.getBooking());

        return mapToPaymentResponse(savedPayment);
    }

    private String generateTransactionId() {
        return "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .transactionId(payment.getTransactionId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentTime(payment.getPaymentTime())
                .build();
    }
}