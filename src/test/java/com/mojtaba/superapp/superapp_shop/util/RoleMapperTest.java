package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.CreateRoleDto;
import com.mojtaba.superapp.superapp_shop.dto.RoleDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateRoleDto;
import com.mojtaba.superapp.superapp_shop.entity.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleMapperTest {

    private final RoleMapper mapper = new RoleMapper();

    @Test
    void fromCreateDto_convertsCorrectly() {
        CreateRoleDto dto = new CreateRoleDto("MANAGER");
        Role r = mapper.fromCreateDto(dto);

        assertThat(r).isNotNull();
        assertThat(r.getRoleName()).isEqualTo("MANAGER");
        assertThat(r.getRoleId()).isNull();
    }

    @Test
    void fromCreateDto_trimsWhitespace() {
        CreateRoleDto dto = new CreateRoleDto("  MANAGER  ");
        Role r = mapper.fromCreateDto(dto);

        assertThat(r.getRoleName()).isEqualTo("MANAGER");
    }

    @Test
    void fromUpdateDto_convertsCorrectly() {
        UpdateRoleDto dto = new UpdateRoleDto("SUPERVISOR");
        Role r = mapper.fromUpdateDto(dto);

        assertThat(r).isNotNull();
        assertThat(r.getRoleName()).isEqualTo("SUPERVISOR");
    }

    @Test
    void fromUpdateDto_trimsWhitespace() {
        UpdateRoleDto dto = new UpdateRoleDto("  SUPERVISOR  ");
        Role r = mapper.fromUpdateDto(dto);

        assertThat(r.getRoleName()).isEqualTo("SUPERVISOR");
    }

    @Test
    void toDto_convertsCorrectly() {
        Role r = new Role();
        r.setRoleId(5);
        r.setRoleName("AUDITOR");

        RoleDto dto = mapper.toDto(r);

        assertThat(dto).isNotNull();
        assertThat(dto.getRoleId()).isEqualTo(5);
        assertThat(dto.getRoleName()).isEqualTo("AUDITOR");
    }
}

