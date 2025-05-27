package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.AuthTokenDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAuthTokenDto;
import java.util.List;
import java.util.Optional;

public interface AuthTokenService {
    AuthTokenDto createToken(CreateAuthTokenDto dto);
    Optional<AuthTokenDto> getById(Long id);
    Optional<AuthTokenDto> getByToken(String token);
    List<AuthTokenDto> getByUserId(Long userId);
    void revokeToken(Long id);
}
