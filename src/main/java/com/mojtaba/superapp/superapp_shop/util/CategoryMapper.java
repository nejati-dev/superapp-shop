package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.*;
import com.mojtaba.superapp.superapp_shop.entity.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public Category fromCreateDto(CreateCategoryDto dto, Category parent) {
        Category category = new Category();
        category.setParent(parent);
        category.setTranslations(dto.getTranslations().stream()
                .map(t -> {
                    CategoryTranslation ct = new CategoryTranslation();
                    ct.setCategory(category); // اتصال Category به CategoryTranslation
                    ct.setName(t.getName());
                    ct.setDescription(t.getDescription());
                    CategoryTranslationId id = new CategoryTranslationId();
                    id.setLangCode(t.getLangCode());
                    ct.setId(id);
                    return ct;
                })
                .collect(Collectors.toSet()));
        return category;
    }

    public CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(category.getCategoryId());
        dto.setParentCategoryId(category.getParent() != null ? category.getParent().getCategoryId() : null);
        dto.setTranslations(category.getTranslations().stream()
                .map(this::toTranslationDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public CategoryTranslationDto toTranslationDto(CategoryTranslation ct) {
        CategoryTranslationDto dto = new CategoryTranslationDto();
        dto.setLangCode(ct.getId().getLangCode());
        dto.setName(ct.getName());
        dto.setDescription(ct.getDescription());
        return dto;
    }
}