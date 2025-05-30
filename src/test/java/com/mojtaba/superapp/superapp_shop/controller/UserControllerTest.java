package com.mojtaba.superapp.superapp_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojtaba.superapp.superapp_shop.dto.CreateUserDto;
import com.mojtaba.superapp.superapp_shop.dto.UserDto;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.service.UserService;
import com.mojtaba.superapp.superapp_shop.util.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService svc;

    @Mock
    private UserMapper mapper;

    private MockMvc mvc;
    private ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser_returns201_andBody() throws Exception {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail("a@b.com");
        dto.setPhone("+93700123456");
        dto.setPassword("pass1234");

        User u = new User();
        u.setUserId(42L);

        UserDto out = new UserDto();
        out.setUserId(42L);
        out.setEmail("a@b.com");
        out.setPhone("+93700123456");

        when(mapper.fromCreateDto(dto)).thenReturn(u);
        when(svc.createUser(u)).thenReturn(u);
        when(mapper.toDto(u)).thenReturn(out);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(42))
                .andExpect(jsonPath("$.email").value("a@b.com"));
    }
}