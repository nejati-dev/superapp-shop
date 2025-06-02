package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Role;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRole_savesAndReturnsRole() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.createRole(role);

        assertNotNull(result);
        assertEquals("ADMIN", result.getRoleName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void getRoleById_existingId_returnsRole() {
        Integer id = 1;
        Role role = new Role();
        role.setRoleId(id);

        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.getRoleById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getRoleId());
        verify(roleRepository, times(1)).findById(id);
    }

    @Test
    void getAllRoles_returnsAllRoles() {
        List<Role> roles = Arrays.asList(new Role(), new Role());

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAllRoles();

        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void updateRole_existingId_updatesAndReturnsRole() {
        Integer id = 1;
        Role existingRole = new Role();
        existingRole.setRoleId(id);
        existingRole.setRoleName("USER");

        Role roleDetails = new Role();
        roleDetails.setRoleName("ADMIN");

        when(roleRepository.findById(id)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        Role result = roleService.updateRole(id, roleDetails);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(captor.capture());

        Role capturedRole = captor.getValue();
        assertEquals(id, capturedRole.getRoleId());
        assertEquals("ADMIN", capturedRole.getRoleName());
        assertEquals("ADMIN", result.getRoleName());
    }

    @Test
    void updateRole_nonExistingId_throwsException() {
        Integer id = 1;
        Role roleDetails = new Role();

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.updateRole(id, roleDetails));
        verify(roleRepository, times(1)).findById(id);
        verify(roleRepository, never()).save(any());
    }

    @Test
    void deleteRole_existingId_deletesRole() {
        Integer id = 1;
        Role role = new Role();
        role.setRoleId(id);

        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        roleService.deleteRole(id);

        verify(roleRepository, times(1)).findById(id);
        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    void deleteRole_nonExistingId_throwsException() {
        Integer id = 1;

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.deleteRole(id));
        verify(roleRepository, times(1)).findById(id);
        verify(roleRepository, never()).delete(any());
    }
}