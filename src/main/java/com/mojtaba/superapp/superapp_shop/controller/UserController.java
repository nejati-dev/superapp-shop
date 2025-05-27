package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.dto.CreateUserDto;
import com.mojtaba.superapp.superapp_shop.dto.UserDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateUserDto;
import com.mojtaba.superapp.superapp_shop.util.UserMapper;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // CREATE با اعتبارسنجی
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        User user = userMapper.fromCreateDto(dto);
        if (user == null) {
            throw new IllegalArgumentException("داده‌های کاربر نامعتبر است");
        }
        User saved = userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toDto(saved));
    }

    // READ همه
    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> list = userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // READ یکی
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOne(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    // UPDATE با اعتبارسنجی
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDto dto) {
        User user = userMapper.fromUpdateDto(dto);
        if (user == null) {
            throw new IllegalArgumentException("داده‌های کاربر نامعتبر است");
        }
        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}