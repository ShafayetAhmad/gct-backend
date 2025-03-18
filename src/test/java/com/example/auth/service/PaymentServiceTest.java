package com.example.auth.service;

import com.example.auth.dto.PaymentRequest;
import com.example.auth.dto.PaymentResponse;
import com.example.auth.exception.PaymentException;
import com.example.auth.model.*;
import com.example.auth.repository.BookingRepository;
import com.example.auth.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private VISACheckService visaCheckService;

    @InjectMocks
    private PaymentService paymentService;

    private Booking booking;
    private Payment payment;
    private PaymentRequest request;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setTotalPrice(BigDecimal.valueOf(100));
        booking.setStatus(BookingStatus.PENDING);

        payment = new Payment();
        payment.setId(1L);
        payment.setBooking(booking);
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.SUCCESS);

        request = new PaymentRequest();
        request.setBookingId(1L);
        request.setCardNumber("4111111111111111");
        request.setExpiryDate("12/25");
        request.setCvv("123");
    }

    @Test
    void processPayment_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBookingId(1L)).thenReturn(false);
        when(visaCheckService.processPayment(any(), any(), any(), any())).thenReturn(true);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = paymentService.processPayment(request);

        assertNotNull(response);
        assertEquals(PaymentStatus.SUCCESS, response.getStatus());
        verify(bookingRepository).save(booking);
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    void processPayment_AlreadyProcessed() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBookingId(1L)).thenReturn(true);

        assertThrows(PaymentException.class, () -> paymentService.processPayment(request));
    }
}