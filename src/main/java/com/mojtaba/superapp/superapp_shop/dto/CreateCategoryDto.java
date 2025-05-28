package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateCategoryDto {
    private Integer parentId;

    @NotNull
    @NotEmpty
    private List<CategoryTranslationDto> translations;
}
