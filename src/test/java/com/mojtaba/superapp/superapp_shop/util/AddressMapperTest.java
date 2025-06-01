package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.AddressDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAddressDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateAddressDto;
import com.mojtaba.superapp.superapp_shop.entity.Address;
import com.mojtaba.superapp.superapp_shop.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

import static org.assertj.core.api.Assertions.*;

class AddressMapperTest {

    private AddressMapper mapper;
    private final WKTReader reader = new WKTReader();
    private User dummyUser;

    @BeforeEach
    void setUp() {
        mapper = new AddressMapper();
        // create a dummy user with ID for mapping
        dummyUser = new User();
        dummyUser.setUserId(123L);
    }

    @Test
    void fromCreateDto_validInput_mapsAllFieldsAndParsesPoint() throws Exception {
        CreateAddressDto dto = new CreateAddressDto();
        dto.setUserId(dummyUser.getUserId());
        dto.setLabel("LabelX");
        dto.setFullAddress("Full Street");
        dto.setCity("CityZ");
        dto.setProvince("ProvinceY");
        dto.setPostalCode("PC999");
        dto.setLocationWkt("POINT(10 20)");

        Address address = mapper.fromCreateDto(dto, dummyUser);

        assertThat(address.getUser()).isEqualTo(dummyUser);
        assertThat(address.getLabel()).isEqualTo("LabelX");
        assertThat(address.getFullAddress()).isEqualTo("Full Street");
        assertThat(address.getCity()).isEqualTo("CityZ");
        assertThat(address.getProvince()).isEqualTo("ProvinceY");
        assertThat(address.getPostalCode()).isEqualTo("PC999");
        Point p = address.getLocation();
        assertThat(p).isNotNull();
        assertThat(p.getX()).isEqualTo(10.0);
        assertThat(p.getY()).isEqualTo(20.0);
    }

    @Test
    void fromCreateDto_invalidWkt_throwsIllegalArgument() {
        CreateAddressDto dto = new CreateAddressDto();
        dto.setUserId(dummyUser.getUserId());
        dto.setLabel("L");
        dto.setFullAddress("F");
        dto.setLocationWkt("INVALID_WKT");

        assertThatThrownBy(() -> mapper.fromCreateDto(dto, dummyUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid WKT");
    }

    @Test
    void updateFromDto_shouldUpdateOnlyNonNullFields() throws Exception {
        Address existing = new Address();
        existing.setLabel("OldLabel");
        existing.setFullAddress("OldFull");
        existing.setCity("OldCity");
        existing.setProvince("OldProv");
        existing.setPostalCode("OldPC");
        existing.setLocation((Point) reader.read("POINT(0 0)"));

        UpdateAddressDto dto = new UpdateAddressDto();
        dto.setLabel("NewLabel");
        // leave fullAddress null
        dto.setCity("NewCity");
        // leave province null
        dto.setPostalCode("NewPC");
        dto.setLocationWkt("POINT(1 1)");

        mapper.updateFromDto(existing, dto);

        // Fields that were not set in UpdateAddressDto remain unchanged
        assertThat(existing.getLabel()).isEqualTo("NewLabel");
        assertThat(existing.getCity()).isEqualTo("NewCity");
        assertThat(existing.getPostalCode()).isEqualTo("NewPC");
        // Unchanged fields:
        assertThat(existing.getFullAddress()).isEqualTo("OldFull");
        assertThat(existing.getProvince()).isEqualTo("OldProv");

        // Location was overwritten
        assertThat(existing.getLocation().getX()).isEqualTo(1.0);
        assertThat(existing.getLocation().getY()).isEqualTo(1.0);
    }

    @Test
    void toDto_shouldCopyAllFieldsAndConvertPointToWkt() throws Exception {
        Address addr = new Address();
        addr.setAddressId(42L);
        User u = new User();
        u.setUserId(314L);
        addr.setUser(u);
        addr.setLabel("MyLabel");
        addr.setFullAddress("Somewhere");
        addr.setCity("C1");
        addr.setProvince("P1");
        addr.setPostalCode("ZIP321");
        Point pt = (Point) reader.read("POINT(5 6)");
        addr.setLocation(pt);

        AddressDto dto = mapper.toDto(addr);

        assertThat(dto.getAddressId()).isEqualTo(42L);
        assertThat(dto.getUserId()).isEqualTo(314L);
        assertThat(dto.getLabel()).isEqualTo("MyLabel");
        assertThat(dto.getFullAddress()).isEqualTo("Somewhere");
        assertThat(dto.getCity()).isEqualTo("C1");
        assertThat(dto.getProvince()).isEqualTo("P1");
        assertThat(dto.getPostalCode()).isEqualTo("ZIP321");
        assertThat(dto.getLocationWkt()).isEqualTo("POINT (5 6)");
    }
}

