package com.mojtaba.superapp.superapp_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // ← این import را اضافه کنید
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mojtaba.superapp.superapp_shop.dto.AuthTokenDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAuthTokenDto;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.AuthTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenControllerTest {

    private MockMvc mvc;

    @Mock
    private AuthTokenService svc;

    @InjectMocks
    private AuthTokenController controller;

    private ObjectMapper objectMapper;
    private AuthTokenDto dummyDto;

    @RestControllerAdvice
    static class TestAdvice {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Void> handleNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @BeforeEach
    void setUp() {
        // ایجاد یک ObjectMapper و ثبت JavaTimeModule برای پشتیبانی از Instant
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // ← ثبت ماژول جاوا-تایم
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new TestAdvice())
                .build();

        dummyDto = new AuthTokenDto();
        dummyDto.setTokenId(100L);
        dummyDto.setUserId(42L);
        dummyDto.setToken("abc");
        dummyDto.setTokenType("access");
        dummyDto.setScope("read");
        Instant now = Instant.now();
        dummyDto.setIssuedAt(now);
        dummyDto.setExpiresAt(now.plusSeconds(3600));
        dummyDto.setRevoked(false);
    }

    @Test
    void create_validInput_shouldReturn201AndDto() throws Exception {
        CreateAuthTokenDto createDto = new CreateAuthTokenDto();
        createDto.setUserId(42L);
        createDto.setToken("abc");
        createDto.setTokenType("access");
        createDto.setScope("read");
        createDto.setExpiresAt(Instant.now().plusSeconds(3600));

        when(svc.createToken(any(CreateAuthTokenDto.class))).thenReturn(dummyDto);

        mvc.perform(post("/api/tokens")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tokenId").value(100))
                .andExpect(jsonPath("$.userId").value(42))
                .andExpect(jsonPath("$.token").value("abc"))
                .andExpect(jsonPath("$.tokenType").value("access"))
                .andExpect(jsonPath("$.scope").value("read"))
                .andExpect(jsonPath("$.revoked").value(false));

        verify(svc).createToken(any(CreateAuthTokenDto.class));
    }

    @Test
    void getOne_existing_shouldReturnDto() throws Exception {
        when(svc.getById(100L)).thenReturn(Optional.of(dummyDto));

        mvc.perform(get("/api/tokens/100")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenId").value(100))
                .andExpect(jsonPath("$.userId").value(42));

        verify(svc).getById(100L);
    }

    @Test
    void getOne_notFound_shouldReturn404() throws Exception {
        when(svc.getById(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/tokens/999")
                        .accept("application/json"))
                .andExpect(status().isNotFound());

        verify(svc).getById(999L);
    }

    @Test
    void getByUser_existing_returnsList() throws Exception {
        AuthTokenDto dto2 = new AuthTokenDto();
        dto2.setTokenId(101L);
        dto2.setUserId(42L);
        dto2.setToken("def");
        dto2.setTokenType("refresh");
        dto2.setScope("write");
        dto2.setIssuedAt(Instant.now());
        dto2.setExpiresAt(Instant.now().plusSeconds(3600));
        dto2.setRevoked(false);

        when(svc.getByUserId(42L)).thenReturn(Arrays.asList(dummyDto, dto2));

        mvc.perform(get("/api/tokens")
                        .param("userId", "42")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tokenId").value(100))
                .andExpect(jsonPath("$[1].tokenId").value(101));

        verify(svc).getByUserId(42L);
    }

    @Test
    void getByUser_noTokens_returnsEmptyList() throws Exception {
        when(svc.getByUserId(55L)).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/tokens")
                        .param("userId", "55")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(svc).getByUserId(55L);
    }

    @Test
    void revoke_existing_shouldReturn204() throws Exception {
        doNothing().when(svc).revokeToken(100L);

        mvc.perform(delete("/api/tokens/100"))
                .andExpect(status().isNoContent());

        verify(svc).revokeToken(100L);
    }

    @Test
    void revoke_notFound_shouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Token not found", "id", 999L))
                .when(svc).revokeToken(999L);

        mvc.perform(delete("/api/tokens/999"))
                .andExpect(status().isNotFound());

        verify(svc).revokeToken(999L);
    }
}
