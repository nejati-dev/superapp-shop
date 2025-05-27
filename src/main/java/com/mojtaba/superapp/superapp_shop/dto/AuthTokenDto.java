package com.mojtaba.superapp.superapp_shop.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class AuthTokenDto {
    private Long tokenId;
    private Long userId;
    private String token;
    private String tokenType;
    private String scope;
    private Instant issuedAt;
    private Instant expiresAt;
    private boolean revoked;
}
