package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Address;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository repo;

    @InjectMocks
    private AddressServiceImpl service;

    private Address a1, a2;
    private final GeometryFactory gf = new GeometryFactory();

    @BeforeEach
    void setUp() {
        a1 = new Address();
        a1.setAddressId(1L);
        // For service‐layer tests, we only care that the entity has an ID, not the full JTS details.
        a1.setLabel("Home");
        a2 = new Address();
        a2.setAddressId(2L);
        a2.setLabel("Office");
    }

    @Test
    void create_shouldCallSaveAndReturnEntity() {
        when(repo.save(a1)).thenReturn(a1);

        Address result = service.create(a1);
        verify(repo).save(a1);
        assertThat(result).isEqualTo(a1);
    }

    @Test
    void findById_existing_returnsOptional() {
        when(repo.findById(1L)).thenReturn(Optional.of(a1));
        Optional<Address> found = service.findById(1L);
        verify(repo).findById(1L);
        assertThat(found).isPresent().contains(a1);
    }

    @Test
    void findById_nonExisting_returnsEmpty() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        Optional<Address> found = service.findById(99L);
        assertThat(found).isEmpty();
        verify(repo).findById(99L);
    }

    @Test
    void findByUserId_shouldReturnList() {
        Long userId = 42L;
        when(repo.findByUserUserId(userId)).thenReturn(Arrays.asList(a1, a2));
        List<Address> list = service.findByUserId(userId);
        verify(repo).findByUserUserId(userId);
        assertThat(list).hasSize(2).containsExactlyInAnyOrder(a1, a2);
    }

    @Test
    void findAll_shouldReturnList() {
        when(repo.findAll()).thenReturn(Arrays.asList(a1, a2));
        List<Address> list = service.findAll();
        verify(repo).findAll();
        assertThat(list).hasSize(2).containsExactlyInAnyOrder(a1, a2);
    }

    @Test
    void update_existing_shouldModifyFieldsAndSave() {
        Address updates = new Address();
        updates.setLabel("New Label");
        updates.setFullAddress("New Address");
        updates.setCity("CityX");
        updates.setProvince("ProvY");
        updates.setPostalCode("PC123");
        Point pt = gf.createPoint(new Coordinate(1.23, 4.56));
        updates.setLocation(pt);

        when(repo.findById(1L)).thenReturn(Optional.of(a1));
        when(repo.save(any(Address.class))).thenReturn(a1);

        Address saved = service.update(1L, updates);

        ArgumentCaptor<Address> captor = ArgumentCaptor.forClass(Address.class);
        verify(repo).save(captor.capture());
        Address toSave = captor.getValue();

        assertThat(toSave.getLabel()).isEqualTo("New Label");
        assertThat(toSave.getFullAddress()).isEqualTo("New Address");
        assertThat(toSave.getCity()).isEqualTo("CityX");
        assertThat(toSave.getProvince()).isEqualTo("ProvY");
        assertThat(toSave.getPostalCode()).isEqualTo("PC123");
        assertThat(toSave.getLocation()).isEqualTo(pt);

        assertThat(saved).isEqualTo(a1);
    }

    @Test
    void update_nonExisting_shouldThrow() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(99L, new Address()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Address not found with");
    }

    @Test
    void delete_existing_shouldCallDeleteById() {
        when(repo.existsById(1L)).thenReturn(true);
        doNothing().when(repo).deleteById(1L);

        service.delete(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void delete_nonExisting_shouldThrow() {
        when(repo.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Address not found with");
    }
}

