package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.dto.AuthTokenDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAuthTokenDto;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.AuthTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
public class AuthTokenController {

    private final AuthTokenService svc;

    public AuthTokenController(AuthTokenService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<AuthTokenDto> create(@Valid @RequestBody CreateAuthTokenDto dto) {
        AuthTokenDto created = svc.createToken(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthTokenDto> getOne(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found", "id", id));
    }

    @GetMapping
    public ResponseEntity<List<AuthTokenDto>> getByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(svc.getByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Long id) {
        svc.revokeToken(id);
        return ResponseEntity.noContent().build();
    }
}
