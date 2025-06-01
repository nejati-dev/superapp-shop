package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.dto.AddressDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAddressDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateAddressDto;
import com.mojtaba.superapp.superapp_shop.entity.Address;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.AddressService;
import com.mojtaba.superapp.superapp_shop.service.UserService;
import com.mojtaba.superapp.superapp_shop.util.AddressMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;
    private final AddressMapper mapper;

    public AddressController(AddressService addressService, UserService userService, AddressMapper mapper) {
        this.addressService = addressService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<AddressDto> create(
            @Valid @RequestBody CreateAddressDto dto) {
        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with", "id", dto.getUserId()));
        Address address = mapper.fromCreateDto(dto, user);
        Address saved = addressService.create(address);
        return ResponseEntity
                .status(201)
                .body(mapper.toDto(saved));
    }

    @GetMapping
    public List<AddressDto> getAll() {
        return addressService.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    public List<AddressDto> getByUser(@PathVariable Long userId) {
        return addressService.findByUserId(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AddressDto getOne(@PathVariable Long id) {
        return addressService.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with", "id", id));
    }

    @PutMapping("/{id}")
    public AddressDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAddressDto dto) {
        Address address = addressService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with", "id", id));
        mapper.updateFromDto(address, dto);
        Address updated = addressService.update(id, address);
        return mapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}