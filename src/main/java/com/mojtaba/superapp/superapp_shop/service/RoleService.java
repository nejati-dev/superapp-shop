package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role createRole(Role role);
    Optional<Role> getRoleById(Integer id);
    List<Role> getAllRoles();
    Role updateRole(Integer id, Role roleDetails);
    void deleteRole(Integer id);
}

