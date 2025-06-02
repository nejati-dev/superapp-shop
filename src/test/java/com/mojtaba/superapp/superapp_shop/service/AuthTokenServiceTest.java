package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.AuthTokenDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAuthTokenDto;
import com.mojtaba.superapp.superapp_shop.entity.AuthToken;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.AuthTokenRepository;
import com.mojtaba.superapp.superapp_shop.repository.UserRepository;
import com.mojtaba.superapp.superapp_shop.util.AuthTokenMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenServiceTest {

    @Mock
    private AuthTokenRepository repo;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthTokenMapper mapper;

    @InjectMocks
    private AuthTokenServiceImpl service;

    private CreateAuthTokenDto createDto;
    private User dummyUser;
    private AuthToken dummyEntity;
    private AuthTokenDto dummyDto;

    @BeforeEach
    void setUp() {
        createDto = new CreateAuthTokenDto();
        createDto.setUserId(42L);
        createDto.setToken("abc123");
        createDto.setTokenType("access");
        createDto.setScope("read");

        // مثالِ یک User ساده
        dummyUser = new User();
        dummyUser.setUserId(42L);

        // نمونهٔ AuthToken (Entity) که mapper قرار است بسازد
        dummyEntity = new AuthToken();
        dummyEntity.setTokenId(100L);
        dummyEntity.setUser(dummyUser);
        dummyEntity.setToken("abc123");
        dummyEntity.setTokenType("access");
        dummyEntity.setScope("read");
        dummyEntity.setIssuedAt(Instant.now());
        dummyEntity.setExpiresAt(Instant.now().plusSeconds(3600));
        dummyEntity.setRevoked(false);

        // DTO خروجی که mapper قرار است برگرداند
        dummyDto = new AuthTokenDto();
        dummyDto.setTokenId(100L);
        dummyDto.setUserId(42L);
        dummyDto.setToken("abc123");
        dummyDto.setTokenType("access");
        dummyDto.setScope("read");
        dummyDto.setIssuedAt(dummyEntity.getIssuedAt());
        dummyDto.setExpiresAt(dummyEntity.getExpiresAt());
        dummyDto.setRevoked(false);
    }

    @Test
    void createToken_existingUser_savesAndReturnsDto() {
        // وقتی userRepository.findById فراخوانی شود، dummyUser را برگردان
        when(userRepository.findById(42L)).thenReturn(Optional.of(dummyUser));
        // وقتی mapper.fromCreateDto صدا زده شود، dummyEntity را برگردان
        when(mapper.fromCreateDto(createDto, dummyUser)).thenReturn(dummyEntity);
        // وقتی repo.save(dummyEntity) فراخوانی شود، همان dummyEntity را برگردان
        when(repo.save(dummyEntity)).thenReturn(dummyEntity);
        // وقتی mapper.toDto(dummyEntity) فراخوانی شود، dummyDto را برگردان
        when(mapper.toDto(dummyEntity)).thenReturn(dummyDto);

        // اجرای متد
        AuthTokenDto result = service.createToken(createDto);

        // بررسی نتیجه
        assertThat(result).isEqualTo(dummyDto);

        verify(userRepository).findById(42L);
        verify(mapper).fromCreateDto(createDto, dummyUser);
        verify(repo).save(dummyEntity);
        verify(mapper).toDto(dummyEntity);
    }

    @Test
    void createToken_nonExistingUser_throwsResourceNotFound() {
        // وقتی userRepository.findById(99L) فراخوانی شود، Optional.empty برگردان
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        createDto.setUserId(99L);

        assertThatThrownBy(() -> service.createToken(createDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findById(99L);
        verifyNoInteractions(mapper);
        verifyNoInteractions(repo);
    }

    @Test
    void getById_existing_returnsDto() {
        when(repo.findById(100L)).thenReturn(Optional.of(dummyEntity));
        when(mapper.toDto(dummyEntity)).thenReturn(dummyDto);

        Optional<AuthTokenDto> result = service.getById(100L);

        assertThat(result).isPresent().get().isEqualTo(dummyDto);
        verify(repo).findById(100L);
        verify(mapper).toDto(dummyEntity);
    }

    @Test
    void getById_nonExisting_returnsEmpty() {
        when(repo.findById(200L)).thenReturn(Optional.empty());

        Optional<AuthTokenDto> result = service.getById(200L);

        assertThat(result).isEmpty();
        verify(repo).findById(200L);
        verifyNoInteractions(mapper);
    }

    @Test
    void getByToken_existing_returnsDto() {
        when(repo.findByToken("abc123")).thenReturn(Optional.of(dummyEntity));
        when(mapper.toDto(dummyEntity)).thenReturn(dummyDto);

        Optional<AuthTokenDto> result = service.getByToken("abc123");

        assertThat(result).isPresent().get().isEqualTo(dummyDto);
        verify(repo).findByToken("abc123");
        verify(mapper).toDto(dummyEntity);
    }

    @Test
    void getByToken_nonExisting_returnsEmpty() {
        when(repo.findByToken("nope")).thenReturn(Optional.empty());

        Optional<AuthTokenDto> result = service.getByToken("nope");

        assertThat(result).isEmpty();
        verify(repo).findByToken("nope");
        verifyNoInteractions(mapper);
    }

    @Test
    void getByUserId_existing_returnsList() {
        AuthToken another = new AuthToken();
        another.setTokenId(101L);
        another.setUser(dummyUser);
        another.setToken("xyz789");
        another.setTokenType("refresh");
        another.setScope("write");
        another.setIssuedAt(Instant.now());
        another.setExpiresAt(Instant.now().plusSeconds(3600));
        another.setRevoked(false);

        AuthTokenDto dto2 = new AuthTokenDto();
        dto2.setTokenId(101L);
        dto2.setUserId(42L);
        dto2.setToken("xyz789");
        dto2.setTokenType("refresh");
        dto2.setScope("write");
        dto2.setIssuedAt(another.getIssuedAt());
        dto2.setExpiresAt(another.getExpiresAt());
        dto2.setRevoked(false);

        when(repo.findByUserUserId(42L)).thenReturn(Arrays.asList(dummyEntity, another));
        when(mapper.toDto(dummyEntity)).thenReturn(dummyDto);
        when(mapper.toDto(another)).thenReturn(dto2);

        List<AuthTokenDto> result = service.getByUserId(42L);

        assertThat(result).hasSize(2).containsExactlyInAnyOrder(dummyDto, dto2);
        verify(repo).findByUserUserId(42L);
        verify(mapper).toDto(dummyEntity);
        verify(mapper).toDto(another);
    }

    @Test
    void getByUserId_noTokens_returnsEmptyList() {
        when(repo.findByUserUserId(55L)).thenReturn(Collections.emptyList());

        List<AuthTokenDto> result = service.getByUserId(55L);

        assertThat(result).isEmpty();
        verify(repo).findByUserUserId(55L);
        verifyNoInteractions(mapper);
    }

    @Test
    void revokeToken_existing_setsRevokedTrue() {
        AuthToken toRevoke = new AuthToken();
        toRevoke.setTokenId(100L);
        toRevoke.setRevoked(false);

        when(repo.findById(100L)).thenReturn(Optional.of(toRevoke));
        when(repo.save(toRevoke)).thenReturn(toRevoke);

        service.revokeToken(100L);

        assertThat(toRevoke.isRevoked()).isTrue();
        verify(repo).findById(100L);
        verify(repo).save(toRevoke);
    }

    @Test
    void revokeToken_nonExisting_throwsResourceNotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.revokeToken(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Token not found");

        verify(repo).findById(999L);
        verify(repo, never()).save(any());
    }
}
