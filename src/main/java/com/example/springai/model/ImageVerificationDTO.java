package com.example.springai.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class ImageVerificationDTO {
    // Getters and setters
    @NotBlank
    @Pattern(regexp = "^https?://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}/?.*$", message = "Invalid URL format")
    private String url;

    private String driverId;
}
