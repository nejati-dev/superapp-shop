package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Role;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // ایجاد نقش جدید
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    // گرفتن نقش با آیدی
    public Optional<Role> getRoleById(Integer id) {
        return roleRepository.findById(id);
    }

    // گرفتن همه نقش‌ها
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    //آپدیت نقش ها
    public Role updateRole(Integer id, Role roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        role.setRoleName(roleDetails.getRoleName());
        return roleRepository.save(role);
    }

    // حذف نقش
    public void deleteRole(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        roleRepository.delete(role);
    }
}