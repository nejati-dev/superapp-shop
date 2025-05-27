package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "auth_tokens", schema = "superapp")
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Token cannot be empty")
    private String token;

    @Column(nullable = false)
    @NotBlank(message = "Token type cannot be empty")
    private String tokenType; // "access" یا "refresh"

    private String scope;

    @Column(nullable = false)
    @NotNull(message = "Issued at cannot be null")
    private Instant issuedAt;

    @Column(nullable = false)
    @NotNull(message = "Expires at cannot be null")
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked;
}
