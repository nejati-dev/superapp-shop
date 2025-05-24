package com.mojtaba.superapp.superapp_shop.dto;

import lombok.Data;

@Data
public class AddressDto {
    private Long addressId;
    private Long userId;
    private String label;
    private String fullAddress;
    private String city;
    private String province;
    private String postalCode;
    private String locationWkt; // مثلاً "POINT(lon lat)"
}
