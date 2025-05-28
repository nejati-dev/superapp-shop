package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateProductDto {
    @NotNull private Integer categoryId;
    @NotNull @Positive private BigDecimal price;
    private List<ProductTranslationDto> translations;
    private List<String> imageUrls;
}
