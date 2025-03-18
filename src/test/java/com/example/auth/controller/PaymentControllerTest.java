package com.example.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.auth.dto.PaymentRequest;
import com.example.auth.dto.PaymentResponse;
import com.example.auth.model.PaymentStatus;
import com.example.auth.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        jwtToken = "Bearer test-jwt-token";
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void processPayment_Success() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setBookingId(1L);
        request.setCardNumber("4111111111111111");
        request.setExpiryDate("12/25");
        request.setCvv("123");

        PaymentResponse mockResponse = PaymentResponse.builder()
                .transactionId("test-transaction")
                .bookingId(1L)
                .amount(BigDecimal.valueOf(100.0))
                .status(PaymentStatus.SUCCESS)
                .build();

        when(paymentService.processPayment(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/payments/process")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void refundPayment_Success() throws Exception {
        PaymentResponse mockResponse = PaymentResponse.builder()
                .transactionId("test-transaction")
                .bookingId(1L)
                .amount(BigDecimal.valueOf(100.0))
                .status(PaymentStatus.REFUNDED)
                .build();

        when(paymentService.refundPayment(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/payments/1/refund")
                .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }
}