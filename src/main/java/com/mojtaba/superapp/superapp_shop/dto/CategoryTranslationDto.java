package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CategoryTranslationDto {
    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    @NotBlank(message = "Language code cannot be blank")
    @Pattern(regexp = "[a-z]{2}", message = "Language code must be exactly 2 lowercase letters")
    private String langCode;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;
}