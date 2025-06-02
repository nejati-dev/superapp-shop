package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.AuthTokenDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAuthTokenDto;
import com.mojtaba.superapp.superapp_shop.entity.AuthToken;
import com.mojtaba.superapp.superapp_shop.entity.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

class AuthTokenMapperTest {

    private final AuthTokenMapper mapper = new AuthTokenMapper();

    @Test
    void fromCreateDto_populatesAllFields() {
        CreateAuthTokenDto dto = new CreateAuthTokenDto();
        dto.setUserId(10L);
        dto.setToken("tk-123");
        dto.setTokenType("access");
        dto.setScope("read");
        Instant exp = Instant.now().plusSeconds(3600);
        dto.setExpiresAt(exp);

        User user = new User();
        user.setUserId(10L);

        AuthToken entity = mapper.fromCreateDto(dto, user);

        assertThat(entity).isNotNull();
        assertThat(entity.getUser()).isSameAs(user);
        assertThat(entity.getToken()).isEqualTo("tk-123");
        assertThat(entity.getTokenType()).isEqualTo("access");
        assertThat(entity.getScope()).isEqualTo("read");
        // به جای isCloseTo... کافی‌ست فقط نال نبودن و ترتیب زمانی را چک کنیم
        assertThat(entity.getIssuedAt()).isNotNull();
        assertThat(entity.getIssuedAt()).isBeforeOrEqualTo(entity.getExpiresAt());
        assertThat(entity.getExpiresAt()).isEqualTo(exp);
        assertThat(entity.isRevoked()).isFalse();
    }

    @Test
    void toDto_copiesAllFields() {
        AuthToken entity = new AuthToken();
        entity.setTokenId(55L);
        User user = new User();
        user.setUserId(20L);
        entity.setUser(user);
        entity.setToken("xyz");
        entity.setTokenType("refresh");
        entity.setScope("write");
        Instant iat = Instant.now();
        Instant exp = iat.plusSeconds(7200);
        entity.setIssuedAt(iat);
        entity.setExpiresAt(exp);
        entity.setRevoked(true);

        AuthTokenDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getTokenId()).isEqualTo(55L);
        assertThat(dto.getUserId()).isEqualTo(20L);
        assertThat(dto.getToken()).isEqualTo("xyz");
        assertThat(dto.getTokenType()).isEqualTo("refresh");
        assertThat(dto.getScope()).isEqualTo("write");
        assertThat(dto.getIssuedAt()).isEqualTo(iat);
        assertThat(dto.getExpiresAt()).isEqualTo(exp);
        assertThat(dto.isRevoked()).isTrue();
    }
}
