package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User u1;
    private User u2;

    @BeforeEach
    void setup() {
        u1 = new User();
        u1.setUserId(1L);
        u1.setEmail("a@b.com");

        u2 = new User();
        u2.setUserId(2L);
        u2.setEmail("c@d.com");
    }

    @Test
    void whenCreateUser_thenRepositorySaveCalled() {
        when(userRepository.save(any(User.class))).thenReturn(u1);

        User created = userService.createUser(u1);

        verify(userRepository).save(u1);
        assertThat(created).isEqualTo(u1);
    }

    @Test
    void whenGetUserById_exists_thenReturnsOptionalOfUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(u1));

        Optional<User> found = userService.getUserById(1L);

        verify(userRepository).findById(1L);
        assertThat(found).isPresent()
                .contains(u1);
    }

    @Test
    void whenGetUserById_notExists_thenReturnsEmptyOptional() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> found = userService.getUserById(99L);

        verify(userRepository).findById(99L);
        assertThat(found).isNotPresent();
    }

    @Test
    void whenGetAllUsers_thenReturnsList() {
        List<User> all = Arrays.asList(u1, u2);
        when(userRepository.findAll()).thenReturn(all);

        List<User> result = userService.getAllUsers();

        verify(userRepository).findAll();
        assertThat(result).hasSize(2)
                .containsExactlyInAnyOrder(u1, u2);
    }

    @Test
    void whenUpdateUser_exists_thenRepositorySaveCalled() {
        User updatedInfo = new User();
        updatedInfo.setEmail("new@e.com");
        updatedInfo.setPhone("123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(userRepository.save(any(User.class))).thenReturn(u1);

        User updated = userService.updateUser(1L, updatedInfo);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User toSave = captor.getValue();

        assertThat(toSave.getEmail()).isEqualTo("new@e.com");
        assertThat(toSave.getPhone()).isEqualTo("123");
        assertThat(updated).isEqualTo(u1);
    }

    @Test
    void whenUpdateUser_notExists_thenThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.updateUser(99L, new User())
        );
    }

    @Test
    void whenDeleteUser_thenRepositoryDeleteCalled() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
