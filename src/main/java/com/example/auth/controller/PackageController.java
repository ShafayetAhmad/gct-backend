package com.example.auth.controller;

import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.PackageResponse;
import com.example.auth.model.User;
import com.example.auth.service.PackageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class PackageController {
    private final PackageService packageService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PackageResponse>> getUserPackage(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(packageService.getUserPackage(user.getId())));
    }
}