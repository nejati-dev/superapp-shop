package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CategoryTranslationDto {
    @NotBlank
    @Pattern(regexp = "[a-z]{2}", message = "Language code must be exactly 2 lowercase letters")
    private String langCode;

    @NotBlank
    private String name;

    private String description;
}
