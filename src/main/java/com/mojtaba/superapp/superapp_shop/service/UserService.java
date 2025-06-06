package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    void deleteUser(Long id);

    @Transactional(readOnly = true)
    Optional<User> getUserByPhone(String phone);

    @Transactional(readOnly = true)
    Optional<User> getUserByEmail(String email);
}

