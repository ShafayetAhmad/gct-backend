package com.example.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.auth.dto.SeatBookingDto;
import com.example.auth.dto.SeatSelectionRequest;
import com.example.auth.dto.BookingResponse;
import com.example.auth.dto.BookedSeatDto;
import com.example.auth.model.DiscountType;
import com.example.auth.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.auth.BaseControllerTest;
import com.example.auth.model.BookingStatus;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private String jwtToken;
    private BookingResponse mockBookingResponse;

    @BeforeEach
    void setUp() {

        // Create a reusable mock response
        mockBookingResponse = BookingResponse.builder()
                .id(1L)
                .performanceTitle("Hamlet")
                .performanceDateTime(LocalDateTime.now())
                .seats(new ArrayList<>())
                .totalPrice(BigDecimal.valueOf(100))
                .status(BookingStatus.CONFIRMED)
                .bookingTime(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createBooking_Success() throws Exception {
        SeatSelectionRequest request = new SeatSelectionRequest();
        request.setPerformanceId(1L);
        request.setSeats(List.of(new SeatBookingDto(1L, DiscountType.NONE)));

        when(bookingService.createBooking(anyLong(), any())).thenReturn(mockBookingResponse);

        mockMvc.perform(post("/api/bookings")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getMyBookings_Success() throws Exception {
        List<BookingResponse> mockBookings = new ArrayList<>();
        mockBookings.add(mockBookingResponse);
        when(bookingService.getUserBookings(anyLong())).thenReturn(mockBookings);

        mockMvc.perform(get("/api/bookings/my-bookings")
                .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void cancelBooking_Success() throws Exception {
        BookingResponse cancelledBooking = BookingResponse.builder()
                .id(1L)
                .performanceTitle("Hamlet")
                .performanceDateTime(LocalDateTime.now())
                .seats(new ArrayList<>())
                .totalPrice(BigDecimal.valueOf(100))
                .status(BookingStatus.CANCELLED)
                .bookingTime(LocalDateTime.now())
                .build();

        when(bookingService.cancelBooking(anyLong(), any())).thenReturn(cancelledBooking);

        mockMvc.perform(post("/api/bookings/1/cancel")
                .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }
}