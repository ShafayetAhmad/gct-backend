package com.example.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.auth.dto.ReviewRequest;
import com.example.auth.dto.ReviewResponse;
import com.example.auth.model.ReviewStatus;
import com.example.auth.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        jwtToken = "Bearer test-jwt-token";
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createReview_Success() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setPlayId(1L);
        request.setRating(5);
        request.setComment("Great play!");

        ReviewResponse mockResponse = ReviewResponse.builder()
                .id(1L)
                .userFullName("John Doe")
                .playTitle("Hamlet")
                .rating(5)
                .comment("Great play!")
                .createdAt(LocalDateTime.now())
                .status(ReviewStatus.PENDING)
                .build();

        when(reviewService.createReview(anyLong(), any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/reviews")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void getPlayReviews_Success() throws Exception {
        List<ReviewResponse> mockReviews = new ArrayList<>();
        when(reviewService.getPlayReviews(anyLong())).thenReturn(mockReviews);

        mockMvc.perform(get("/api/reviews/play/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void moderateReview_Success() throws Exception {
        ReviewResponse mockResponse = ReviewResponse.builder()
                .id(1L)
                .userFullName("John Doe")
                .playTitle("Hamlet")
                .rating(5)
                .comment("Great play!")
                .createdAt(LocalDateTime.now())
                .status(ReviewStatus.APPROVED)
                .build();

        when(reviewService.moderateReview(anyLong(), any(ReviewStatus.class))).thenReturn(mockResponse);

        mockMvc.perform(patch("/api/reviews/1/moderate")
                .header("Authorization", jwtToken)
                .param("status", ReviewStatus.APPROVED.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }
}