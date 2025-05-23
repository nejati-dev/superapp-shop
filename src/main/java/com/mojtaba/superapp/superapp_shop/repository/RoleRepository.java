package com.mojtaba.superapp.superapp_shop.repository;


import com.mojtaba.superapp.superapp_shop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>{
    Optional<Role> findByRoleName(String roleName);
    List<Role> findAllByRoleNameIn(Collection<String> roleNames);
}
