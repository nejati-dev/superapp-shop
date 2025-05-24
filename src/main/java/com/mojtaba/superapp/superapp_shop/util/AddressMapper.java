package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.AddressDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAddressDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateAddressDto;
import com.mojtaba.superapp.superapp_shop.entity.Address;
import com.mojtaba.superapp.superapp_shop.entity.User;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Address DTOs and entities.
 */
@Component
public class AddressMapper {

    private final WKTReader wktReader = new WKTReader();

    /**
     * Converts CreateAddressDto to Address entity.
     * Note: User entity must be set by the service layer.
     *
     * @param dto the CreateAddressDto
     * @return the Address entity
     */
    public Address fromCreateDto(CreateAddressDto dto) {
        Address address = new Address();
        address.setLabel(dto.getLabel());
        address.setFullAddress(dto.getFullAddress());
        address.setCity(dto.getCity());
        address.setProvince(dto.getProvince());
        address.setPostalCode(dto.getPostalCode());
        address.setLocation(parseWkt(dto.getLocationWkt()));
        return address;
    }

    /**
     * Updates an existing Address entity from UpdateAddressDto.
     *
     * @param address the Address entity to update
     * @param dto the UpdateAddressDto
     */
    public void updateFromDto(Address address, UpdateAddressDto dto) {
        if (dto.getLabel() != null) {
            address.setLabel(dto.getLabel());
        }
        if (dto.getFullAddress() != null) {
            address.setFullAddress(dto.getFullAddress());
        }
        if (dto.getCity() != null) {
            address.setCity(dto.getCity());
        }
        if (dto.getProvince() != null) {
            address.setProvince(dto.getProvince());
        }
        if (dto.getPostalCode() != null) {
            address.setPostalCode(dto.getPostalCode());
        }
        if (dto.getLocationWkt() != null) {
            address.setLocation(parseWkt(dto.getLocationWkt()));
        }
    }

    /**
     * Converts Address entity to AddressDto.
     *
     * @param address the Address entity
     * @return the AddressDto
     */
    public AddressDto toDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setAddressId(address.getAddressId());
        dto.setUserId(address.getUser().getUserId());
        dto.setLabel(address.getLabel());
        dto.setFullAddress(address.getFullAddress());
        dto.setCity(address.getCity());
        dto.setProvince(address.getProvince());
        dto.setPostalCode(address.getPostalCode());
        dto.setLocationWkt(address.getLocation().toText());
        return dto;
    }

    /**
     * Parses WKT string to Point.
     *
     * @param wkt the WKT string
     * @return the Point object
     */
    private Point parseWkt(String wkt) {
        try {
            return (Point) wktReader.read(wkt);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid WKT: " + wkt, e);
        }
    }
}