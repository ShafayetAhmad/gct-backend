package com.example.auth.controller;

import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.UserResponse;
import com.example.auth.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal User user) {
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName());
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }
}