package com.example.auth.controller;

import com.example.auth.dto.PerformanceRequest;
import com.example.auth.model.User;
import com.example.auth.model.UserRole;
import com.example.auth.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PerformanceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private String adminToken;
    private String customerToken;

    @BeforeEach
    void setUp() {
        User admin = new User();
        admin.setEmail("admin@test.com");
        admin.setRole(UserRole.ADMIN);
        adminToken = jwtUtil.generateToken(admin);

        User customer = new User();
        customer.setEmail("customer@test.com");
        customer.setRole(UserRole.CUSTOMER);
        customerToken = jwtUtil.generateToken(customer);
    }

    @Test
    void createPerformance_AdminSuccess() throws Exception {
        PerformanceRequest request = new PerformanceRequest();
        request.setPlayId(1L);
        request.setDateTime(LocalDateTime.now().plusDays(1));
        request.setBasePrice(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/performances")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void createPerformance_CustomerForbidden() throws Exception {
        PerformanceRequest request = new PerformanceRequest();
        request.setPlayId(1L);
        request.setDateTime(LocalDateTime.now().plusDays(1));
        request.setBasePrice(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/performances")
                .header("Authorization", "Bearer " + customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}