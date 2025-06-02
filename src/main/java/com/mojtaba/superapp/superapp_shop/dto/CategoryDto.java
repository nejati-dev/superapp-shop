package com.mojtaba.superapp.superapp_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Integer categoryId;
    private Integer parentCategoryId;
    private List<CategoryTranslationDto> translations;
}
