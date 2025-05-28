package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProductDto {
    @NotNull private Integer categoryId;
    @NotBlank private String sku;
    @NotNull @Positive private BigDecimal price;
    private List<ProductTranslationDto> translations;
    private List<String> imageUrls;
}
