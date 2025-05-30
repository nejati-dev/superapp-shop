package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User u1, u2;

    @BeforeEach
    void setUp() {
        u1 = new User(); u1.setUserId(1L); u1.setEmail("a@b.com"); u1.setPhone("+111");
        u2 = new User(); u2.setUserId(2L); u2.setEmail("b@c.com"); u2.setPhone("+222");
    }

    @Test
    void createUser_callsSaveAndReturnsEntity() {
        when(userRepository.save(u1)).thenReturn(u1);

        User created = userService.createUser(u1);

        verify(userRepository).save(u1);
        assertThat(created).isEqualTo(u1);
    }

    @Test
    void getUserById_nonExisting_returnsEmpty() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(userService.getUserById(99L)).isEmpty();
    }

    @Test
    void getAllUsers_returnsList() {
        List<User> list = Arrays.asList(u1, u2);
        when(userRepository.findAll()).thenReturn(list);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2).containsExactlyInAnyOrder(u1, u2);
    }

    @Test
    void updateUser_existing_updatesFields() {
        User updates = new User();
        updates.setEmail("new@e.com");
        updates.setPhone("+93700999999");

        when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(userRepository.save(any(User.class))).thenReturn(u1);

        User saved = userService.updateUser(1L, updates);

        ArgumentCaptor<User> cap = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(cap.capture());
        User toSave = cap.getValue();

        assertThat(toSave.getEmail()).isEqualTo("new@e.com");
        assertThat(toSave.getPhone()).isEqualTo("+93700999999");
        assertThat(saved).isEqualTo(u1);
    }

    @Test
    void updateUser_nonExisting_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(99L, new User())
        );
    }

    @Test
    void deleteUser_existing_callsDelete() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_nonExisting_throws() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(99L)
        );
    }
}
