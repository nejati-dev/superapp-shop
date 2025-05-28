package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.AuthTokenDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAuthTokenDto;
import com.mojtaba.superapp.superapp_shop.entity.AuthToken;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.AuthTokenRepository;
import com.mojtaba.superapp.superapp_shop.repository.UserRepository;
import com.mojtaba.superapp.superapp_shop.util.AuthTokenMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthTokenServiceImpl implements AuthTokenService {

    private final AuthTokenRepository repo;
    private final AuthTokenMapper mapper;
    private final UserRepository userRepository;

    public AuthTokenServiceImpl(AuthTokenRepository repo, AuthTokenMapper mapper, UserRepository userRepository) {
        this.repo = repo;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Override
    public AuthTokenDto createToken(CreateAuthTokenDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: ", "id", dto.getUserId()));
        AuthToken entity = mapper.fromCreateDto(dto, user);
        return mapper.toDto(repo.save(entity));
    }

    @Override
    public Optional<AuthTokenDto> getById(Long id) {
        return repo.findById(id).map(mapper::toDto);
    }

    @Override
    public Optional<AuthTokenDto> getByToken(String token) {
        return repo.findByToken(token).map(mapper::toDto);
    }

    @Override
    public List<AuthTokenDto> getByUserId(Long userId) {
        return repo.findByUserUserId(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void revokeToken(Long id) {
        AuthToken t = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found:", "id", id));
        t.setRevoked(true);
        repo.save(t);
    }
}