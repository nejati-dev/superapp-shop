package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.Instant;

@Data
public class CreateAuthTokenDto {
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotBlank(message = "Token cannot be empty")
    private String token;
    @NotBlank(message = "Token type cannot be empty")
    private String tokenType;
    private String scope;
    @NotNull(message = "Expires at cannot be null")
    private Instant expiresAt;
}
