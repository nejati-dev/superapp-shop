package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.CreateRoleDto;
import com.mojtaba.superapp.superapp_shop.dto.RoleDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateRoleDto;
import com.mojtaba.superapp.superapp_shop.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    /**
     * از DTO ایجاد نقش بساز (فیلد roleId را خود بانک‌اطلاعات خواهد گذاشت)
     */
    public Role fromCreateDto(CreateRoleDto dto) {
        Role r = new Role();
        r.setRoleName(dto.getRoleName().trim());
        return r;
    }

    /**
     * از DTO بروزسانی نقش بساز (اینجا id را نمی‌دهیم؛ فقط مقدار جدیدِ roleName)
     */
    public Role fromUpdateDto(UpdateRoleDto dto) {
        Role r = new Role();
        r.setRoleName(dto.getRoleName().trim());
        return r;
    }

    /**
     * از Entity به DTO خروجی
     */
    public RoleDto toDto(Role r) {
        return new RoleDto(r.getRoleId(), r.getRoleName());
    }
}

