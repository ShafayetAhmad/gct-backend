package com.example.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayResponse {
    private Long id;
    private String title;
    private String description;
}