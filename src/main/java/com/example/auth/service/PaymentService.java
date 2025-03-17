package com.example.auth.service;

import com.example.auth.dto.PaymentRequest;
import com.example.auth.exception.PaymentException;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.model.Booking;
import com.example.auth.model.Payment;
import com.example.auth.model.PaymentStatus;
import com.example.auth.model.BookingStatus;
import com.example.auth.repository.BookingRepository;
import com.example.auth.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final MockVisaCheckService visaCheckService;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    public Payment processPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Check if payment already exists
        if (paymentRepository.existsByBookingId(booking.getId())) {
            throw new PaymentException("Payment already processed for this booking");
        }

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
            payment.setStatus(PaymentStatus.COMPLETED);
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            booking.setStatus(BookingStatus.CANCELLED);
            throw new PaymentException("Payment failed");
        }

        return paymentRepository.save(payment);
    }

    public Payment refundPayment(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new PaymentException("Payment cannot be refunded");
        }

        // Mock refund process
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.getBooking().setStatus(BookingStatus.REFUNDED);

        return paymentRepository.save(payment);
    }

    private String generateTransactionId() {
        return "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}