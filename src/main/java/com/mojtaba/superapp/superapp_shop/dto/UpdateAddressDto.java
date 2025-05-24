package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAddressDto {
    private String label;
    private String fullAddress;
    private String city;
    private String province;
    private String postalCode;
    private String locationWkt;
}
