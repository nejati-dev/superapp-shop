package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.CreateUserDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateUserDto;
import com.mojtaba.superapp.superapp_shop.dto.UserDto;
import com.mojtaba.superapp.superapp_shop.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserMapperTest {

    private final PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
    private final UserMapper mapper = new UserMapper(encoder);

    @Test
    void fromCreateDto_mapsAllFieldsAndEncodesPassword() {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail("e@e.com");
        dto.setPhone("+93700123456");
        dto.setPassword("plain");

        when(encoder.encode("plain")).thenReturn("hashed");

        User u = mapper.fromCreateDto(dto);
        assertThat(u.getEmail()).isEqualTo("e@e.com");
        assertThat(u.getPhone()).isEqualTo("+93700123456");
        assertThat(u.getPasswordHash()).isEqualTo("hashed");
    }

    @Test
    void fromUpdateDto_mapsEmailAndPhone() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setEmail("u@u.com");
        dto.setPhone("+93700999999");

        User u = mapper.fromUpdateDto(dto);
        assertThat(u.getEmail()).isEqualTo("u@u.com");
        assertThat(u.getPhone()).isEqualTo("+93700999999");
    }

    @Test
    void toDto_mapsAllFields() {
        User u = new User();
        u.setUserId(5L);
        u.setEmail("z@z.com");
        u.setPhone("+93700123456");

        UserDto dto = mapper.toDto(u);
        assertThat(dto.getUserId()).isEqualTo(5L);
        assertThat(dto.getEmail()).isEqualTo("z@z.com");
        assertThat(dto.getPhone()).isEqualTo("+93700123456");
    }
}

