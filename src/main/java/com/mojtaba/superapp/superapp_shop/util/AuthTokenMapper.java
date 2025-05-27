package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.AuthTokenDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAuthTokenDto;
import com.mojtaba.superapp.superapp_shop.entity.AuthToken;
import com.mojtaba.superapp.superapp_shop.entity.User;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class AuthTokenMapper {

    public AuthToken fromCreateDto(CreateAuthTokenDto dto, User user) {
        AuthToken t = new AuthToken();
        t.setUser(user);
        t.setToken(dto.getToken());
        t.setTokenType(dto.getTokenType());
        t.setScope(dto.getScope());
        t.setIssuedAt(Instant.now());
        t.setExpiresAt(dto.getExpiresAt());
        t.setRevoked(false);
        return t;
    }

    public AuthTokenDto toDto(AuthToken t) {
        AuthTokenDto dto = new AuthTokenDto();
        dto.setTokenId(t.getTokenId());
        dto.setUserId(t.getUser().getUserId());
        dto.setToken(t.getToken());
        dto.setTokenType(t.getTokenType());
        dto.setScope(t.getScope());
        dto.setIssuedAt(t.getIssuedAt());
        dto.setExpiresAt(t.getExpiresAt());
        dto.setRevoked(t.isRevoked());
        return dto;
    }
}
