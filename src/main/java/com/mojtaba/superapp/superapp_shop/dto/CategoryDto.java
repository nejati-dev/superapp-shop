package com.mojtaba.superapp.superapp_shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private Integer categoryId;
    private Integer parentCategoryId;
    private List<CategoryTranslationDto> translations;
}
