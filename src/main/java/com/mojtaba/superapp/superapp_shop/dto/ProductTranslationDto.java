package com.mojtaba.superapp.superapp_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductTranslationDto {
    private String langCode;
    private String name;
    private String description;
}
