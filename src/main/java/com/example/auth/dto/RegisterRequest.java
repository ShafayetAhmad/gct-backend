package com.example.auth.dto;

import lombok.Data;
import com.example.auth.model.UserRole;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private UserRole role = UserRole.CUSTOMER;
}