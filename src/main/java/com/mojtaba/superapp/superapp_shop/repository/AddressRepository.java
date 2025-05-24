package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);
    List<Address> findByUserUserId(Long userId);

}
