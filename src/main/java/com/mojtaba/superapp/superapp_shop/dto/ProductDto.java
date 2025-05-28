package com.mojtaba.superapp.superapp_shop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Integer productId;
    private Integer categoryId;
    private String sku;
    private BigDecimal price;
    private List<ProductTranslationDto> translations;
    private List<String> imageUrls;
}
