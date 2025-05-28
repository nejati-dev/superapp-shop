package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Embeddable
@Data
public class CategoryTranslationId implements java.io.Serializable {
    private Integer categoryId;

    @NotBlank(message = "Language code connot be blank")
    @Size(min = 2, max = 2, message = "Language code must be exactly 2 characters")
    private String langCode;
}
