package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API Response wrapper")
public class ApiResponse<T> {
    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Response message", example = "Success")
    private String message;

    @Schema(description = "Response data")
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}