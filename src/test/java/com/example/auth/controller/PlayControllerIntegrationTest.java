package com.example.auth.controller;

import com.example.auth.dto.PlayRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlayControllerIntegrationTest {

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
    void createPlay_AdminSuccess() throws Exception {
        PlayRequest request = new PlayRequest();
        request.setTitle("Test Play");
        request.setDescription("Test Description");

        mockMvc.perform(post("/api/plays")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.title").value(request.getTitle()));
    }

    @Test
    void createPlay_CustomerForbidden() throws Exception {
        PlayRequest request = new PlayRequest();
        request.setTitle("Test Play");
        request.setDescription("Test Description");

        mockMvc.perform(post("/api/plays")
                .header("Authorization", "Bearer " + customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}