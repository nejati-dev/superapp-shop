package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Address;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repo;

    public AddressServiceImpl(AddressRepository repo) {
        this.repo = repo;
    }

    @Override
    public Address create(Address address) {
        return repo.save(address);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findByUserId(Long userId) {
        return repo.findByUserUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findAll() {
        return repo.findAll();
    }

    @Override
    public Address update(Long id, Address details) {
        Address address = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with", "id", id));
        address.setLabel(details.getLabel());
        address.setFullAddress(details.getFullAddress());
        address.setCity(details.getCity());
        address.setProvince(details.getProvince());
        address.setPostalCode(details.getPostalCode());
        address.setLocation(details.getLocation());
        return repo.save(address);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Address not found with" , "id", id);
        }
        repo.deleteById(id);
    }
}