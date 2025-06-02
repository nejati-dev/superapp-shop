package com.mojtaba.superapp.superapp_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojtaba.superapp.superapp_shop.dto.CreateRoleDto;
import com.mojtaba.superapp.superapp_shop.dto.RoleDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateRoleDto;
import com.mojtaba.superapp.superapp_shop.entity.Role;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.RoleService;
import com.mojtaba.superapp.superapp_shop.util.RoleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.io.ParseException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    private MockMvc mvc;

    @Mock
    private RoleService roleService;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleController roleController;

    private ObjectMapper objectMapper;
    private Role dummyRole;
    private RoleDto dummyDto;

    // این کلاس به عنوان ControllerAdvice برای تبدیل ResourceNotFoundException به 404 ثبت می‌شود
    @RestControllerAdvice
    static class TestAdvice {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Void> handleNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders
                .standaloneSetup(roleController)
                .setControllerAdvice(new TestAdvice())
                .build();

        dummyRole = new Role();
        dummyRole.setRoleId(10);
        dummyRole.setRoleName("ADMIN");

        dummyDto = new RoleDto(10, "ADMIN");
    }

    @Test
    void createRole_validInput_shouldReturn201AndDto() throws Exception {
        CreateRoleDto createDto = new CreateRoleDto("MANAGER");
        Role toSave = new Role();
        toSave.setRoleName("MANAGER");
        Role saved = new Role();
        saved.setRoleId(20);
        saved.setRoleName("MANAGER");
        RoleDto responseDto = new RoleDto(20, "MANAGER");

        when(roleMapper.fromCreateDto(createDto)).thenReturn(toSave);
        when(roleService.createRole(toSave)).thenReturn(saved);
        when(roleMapper.toDto(saved)).thenReturn(responseDto);

        mvc.perform(post("/api/roles")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleId").value(20))
                .andExpect(jsonPath("$.roleName").value("MANAGER"));

        verify(roleMapper).fromCreateDto(createDto);
        verify(roleService).createRole(toSave);
        verify(roleMapper).toDto(saved);
    }

    @Test
    void getAllRoles_whenEmpty_shouldReturnEmptyList() throws Exception {
        when(roleService.getAllRoles()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/roles")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(roleService).getAllRoles();
    }

    @Test
    void getAllRoles_withResults_shouldReturnList() throws Exception {
        Role r1 = new Role();
        r1.setRoleId(1);
        r1.setRoleName("USER");
        RoleDto dto1 = new RoleDto(1, "USER");

        when(roleService.getAllRoles()).thenReturn(Collections.singletonList(r1));
        when(roleMapper.toDto(r1)).thenReturn(dto1);

        mvc.perform(get("/api/roles")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roleId").value(1))
                .andExpect(jsonPath("$[0].roleName").value("USER"));

        verify(roleService).getAllRoles();
        verify(roleMapper).toDto(r1);
    }

    @Test
    void getRoleById_existing_shouldReturnDto() throws Exception {
        when(roleService.getRoleById(10)).thenReturn(Optional.of(dummyRole));
        when(roleMapper.toDto(dummyRole)).thenReturn(dummyDto);

        mvc.perform(get("/api/roles/10")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId").value(10))
                .andExpect(jsonPath("$.roleName").value("ADMIN"));

        verify(roleService).getRoleById(10);
        verify(roleMapper).toDto(dummyRole);
    }

    @Test
    void getRoleById_notFound_shouldReturn404() throws Exception {
        when(roleService.getRoleById(99)).thenReturn(Optional.empty());

        mvc.perform(get("/api/roles/99")
                        .accept("application/json"))
                .andExpect(status().isNotFound());

        verify(roleService).getRoleById(99);
    }

    @Test
    void updateRole_existing_shouldReturnUpdatedDto() throws Exception {
        UpdateRoleDto updateDto = new UpdateRoleDto("SUPERVISOR");
        Role details = new Role();
        details.setRoleName("SUPERVISOR");
        Role updatedEntity = new Role();
        updatedEntity.setRoleId(10);
        updatedEntity.setRoleName("SUPERVISOR");
        RoleDto updatedDto = new RoleDto(10, "SUPERVISOR");

        // حتماً mapper یک شیء غیر-null برمی‌گرداند
        when(roleMapper.fromUpdateDto(updateDto)).thenReturn(details);
        when(roleService.updateRole(10, details)).thenReturn(updatedEntity);
        when(roleMapper.toDto(updatedEntity)).thenReturn(updatedDto);

        mvc.perform(put("/api/roles/10")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId").value(10))
                .andExpect(jsonPath("$.roleName").value("SUPERVISOR"));

        verify(roleMapper).fromUpdateDto(updateDto);
        verify(roleService).updateRole(10, details);
        verify(roleMapper).toDto(updatedEntity);
    }

    @Test
    void updateRole_notFound_shouldReturn404() throws Exception {
        UpdateRoleDto updateDto = new UpdateRoleDto("NON_EXISTENT");
        // حتماً mapper یک Role واقعی برمی‌گرداند (نه null)
        Role dummyDetails = new Role();
        dummyDetails.setRoleName("NON_EXISTENT");
        when(roleMapper.fromUpdateDto(updateDto)).thenReturn(dummyDetails);

        // stub می‌کنیم تا استثناء پرتاب شود
        doThrow(new ResourceNotFoundException("Role", "id", 99))
                .when(roleService).updateRole(99, dummyDetails);

        mvc.perform(put("/api/roles/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());

        verify(roleMapper).fromUpdateDto(updateDto);
        verify(roleService).updateRole(99, dummyDetails);
    }

    @Test
    void deleteRole_existing_shouldReturn204() throws Exception {
        doNothing().when(roleService).deleteRole(10);

        mvc.perform(delete("/api/roles/10"))
                .andExpect(status().isNoContent());

        verify(roleService).deleteRole(10);
    }

    @Test
    void deleteRole_notFound_shouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Role", "id", 123))
                .when(roleService).deleteRole(123);

        mvc.perform(delete("/api/roles/123"))
                .andExpect(status().isNotFound());

        verify(roleService).deleteRole(123);
    }
}
