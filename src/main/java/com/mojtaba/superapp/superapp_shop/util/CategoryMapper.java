package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.CategoryTranslationDto;
import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.CategoryTranslation;
import com.mojtaba.superapp.superapp_shop.entity.CategoryTranslationId;
import com.mojtaba.superapp.superapp_shop.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    private final CategoryRepository catRepo;

    public CategoryMapper(CategoryRepository catRepo) {
        this.catRepo = catRepo;
    }

    public CategoryTranslation toEntity(CategoryTranslationDto tdto) {
        CategoryTranslation ct = new CategoryTranslation();
        // تنظیم id (شامل categoryId و langCode)
        CategoryTranslationId id = new CategoryTranslationId();
        id.setLangCode(tdto.getLangCode());
        id.setCategoryId(tdto.getCategoryId());
        ct.setId(id);

        // تنظیم سایر فیلدها
        ct.setName(tdto.getName());
        ct.setDescription(tdto.getDescription());

        // تنظیم رابطه با Category
        Category category = catRepo.findById(tdto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        ct.setCategory(category);

        return ct;
    }

    public CategoryTranslationDto toDto(CategoryTranslation ct) {
        CategoryTranslationDto dto = new CategoryTranslationDto();
        dto.setCategoryId(ct.getId().getCategoryId());
        dto.setLangCode(ct.getId().getLangCode());
        dto.setName(ct.getName());
        dto.setDescription(ct.getDescription());
        return dto;
    }
}