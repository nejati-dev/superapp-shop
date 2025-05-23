package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.entity.Role;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        Role created = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable Integer id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @GetMapping
    public List<Role> getAll() {
        return roleService.getAllRoles();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> update(
            @PathVariable Integer id,
            @Valid @RequestBody Role role) {
        Role updated = roleService.updateRole(id, role);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
