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
                .map(tdto -> {
                    CategoryTranslation ct = new CategoryTranslation();
                    ct.setCategory(category);
                    ct.setLangCode(tdto.getLangCode());
                    ct.setName(tdto.getName());
                    ct.setDescription(tdto.getDescription());
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
                .map(ct -> {
                    CategoryTranslationDto td = new CategoryTranslationDto();
                    td.setLangCode(ct.getLangCode());
                    td.setName(ct.getName());
                    td.setDescription(ct.getDescription());
                    return td;
                })
                .collect(Collectors.toList()));
        return dto;
    }
}
