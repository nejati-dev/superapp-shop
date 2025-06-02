package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.dto.CreateRoleDto;
import com.mojtaba.superapp.superapp_shop.dto.RoleDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateRoleDto;
import com.mojtaba.superapp.superapp_shop.entity.Role;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.RoleService;
import com.mojtaba.superapp.superapp_shop.util.RoleMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    public RoleController(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    /**
     * ایجاد نقش جدید
     * (ورودی: JSON مطابق CreateRoleDto، خروجی: RoleDto با ID جدید)
     */
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody CreateRoleDto dto) {
        Role entity = roleMapper.fromCreateDto(dto);
        Role saved = roleService.createRole(entity);
        RoleDto response = roleMapper.toDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * لیست همه نقش‌ها
     */
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> list = roleService.getAllRoles().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /**
     * دریافت یک نقش با ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Integer id) {
        RoleDto dto = roleService.getRoleById(id)
                .map(roleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        return ResponseEntity.ok(dto);
    }

    /**
     * بروزرسانی یک نقش (تنها roleName)
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateRoleDto dto) {

        Role details = roleMapper.fromUpdateDto(dto);
        Role updated = roleService.updateRole(id, details);
        RoleDto response = roleMapper.toDto(updated);
        return ResponseEntity.ok(response);
    }

    /**
     * حذف یک نقش
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
