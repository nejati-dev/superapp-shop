package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAddressDto {
    @NotNull
    private Long userId;
    @NotBlank
    private String label;
    @NotBlank
    private String fullAddress;
    private String city;
    private String province;
    private String postalCode;
    @NotBlank
    private String locationWkt;
}

