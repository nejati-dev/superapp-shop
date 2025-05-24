package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    Address create(Address address);
    Optional<Address> findById(Long id);
    List<Address> findByUserId(Long userId);
    Address update(Long id, Address address);
    void delete(Long id);
}
