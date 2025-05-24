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
        return repo.findByUserId(userId);
    }

    @Override
    public Address update(Long id, Address details) {
        Address a = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        a.setLabel(details.getLabel());
        a.setFullAddress(details.getFullAddress());
        a.setCity(details.getCity());
        a.setProvince(details.getProvince());
        a.setPostalCode(details.getPostalCode());
        a.setLocation(details.getLocation());
        return repo.save(a);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        repo.deleteById(id);
    }
}