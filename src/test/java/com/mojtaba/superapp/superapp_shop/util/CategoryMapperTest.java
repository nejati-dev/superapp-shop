package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.CategoryTranslationDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateCategoryDto;
import com.mojtaba.superapp.superapp_shop.dto.CategoryDto;
import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.CategoryTranslation;
import com.mojtaba.superapp.superapp_shop.entity.CategoryTranslationId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.*;

class CategoryMapperTest {

    private CategoryMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CategoryMapper();
    }

    @Test
    @DisplayName("🟢 fromCreateDto باید یک Category با ترجمه‌ها بسازد")
    void fromCreateDto_shouldMapDtoToEntity() {
        // 1) آماده‌سازی یک CategoryTranslationDto که CreateCategoryDto از آن استفاده می‌کند
        CategoryTranslationDto translationDto = new CategoryTranslationDto();
        translationDto.setLangCode("fa");
        translationDto.setName("الکترونیک");
        translationDto.setDescription("دسته‌بندی لوازم الکترونیکی");
        // در CreateCategoryDto فیلد categoryId اهمیتی ندارد و توسط Mapper استفاده نمی‌شود

        // 2) آماده‌سازی CreateCategoryDto و قراردادن لیست ترجمه‌ها
        CreateCategoryDto dto = new CreateCategoryDto();
        dto.setParentId(null);
        dto.setTranslations(Collections.singletonList(translationDto));

        // 3) فراخوانی متد از Mapper
        Category result = mapper.fromCreateDto(dto, /* parent = */ null);

        // 4) بررسی اینکه parent در Category تازه null باشد
        assertThat(result.getParent()).isNull();

        // 5) بررسی اینکه دقیقا یک ترجمه در Categoryِ ساخته‌شده وجود دارد
        assertThat(result.getTranslations()).hasSize(1);

        // 6) برداشتن تنها ترجمه و بررسی فیلدهای آن
        CategoryTranslation ct = result.getTranslations().iterator().next();
        assertThat(ct.getId().getLangCode()).isEqualTo("fa");
        assertThat(ct.getName()).isEqualTo("الکترونیک");
        assertThat(ct.getDescription()).isEqualTo("دسته‌بندی لوازم الکترونیکی");

        // 7) بررسی ارتباط دوطرفه: CategoryTranslation.getCategory باید به شیء Category برگردانده‌شده اشاره کند
        assertThat(ct.getCategory()).isSameAs(result);
    }

    @Test
    @DisplayName("🟢 toDto باید فیلدهای اصلی Category را به CategoryDto منتقل کند")
    void toDto_shouldMapEntityToDto() {
        // 1) آماده‌سازی موجودیت والد و تنظیم شناسه‌ی آن
        Category parent = new Category();
        parent.setCategoryId(5);

        // 2) آماده‌سازی Category اصلی و تعیین والد آن
        Category cat = new Category();
        cat.setCategoryId(10);
        cat.setParent(parent);

        // 3) مقداردهی اولیه به مجموعه‌ی translations (در انتیتی این مجموعه در سازنده null نیست،
        //    اما برای اطمینان صریحاً تنظیم می‌کنیم)
        cat.setTranslations(new HashSet<>());

        // 4) ایجاد CategoryTranslation و افزودن به cat.getTranslations()
        CategoryTranslationId tid = new CategoryTranslationId();
        tid.setCategoryId(10);
        tid.setLangCode("en");

        CategoryTranslation ct = new CategoryTranslation();
        ct.setId(tid);
        ct.setCategory(cat);
        ct.setName("Electronics");
        ct.setDescription("Electronic items");

        cat.getTranslations().add(ct);

        // 5) فراخوانی متد toDto از Mapper
        CategoryDto dto = mapper.toDto(cat);

        // 6) بررسی صحیح بودن فیلدهای خروجی
        assertThat(dto.getCategoryId()).isEqualTo(10);
        assertThat(dto.getParentCategoryId()).isEqualTo(5);

        // چون یک ترجمه ساخته‌ایم، لیست ترجمه‌ها باید دقیقا سایز ۱ باشد
        assertThat(dto.getTranslations()).hasSize(1);

        // 7) بررسی محتویات درون CategoryTranslationDto خروجی
        CategoryTranslationDto transDto = dto.getTranslations().get(0);
        assertThat(transDto.getLangCode()).isEqualTo("en");
        assertThat(transDto.getName()).isEqualTo("Electronics");
        assertThat(transDto.getDescription()).isEqualTo("Electronic items");
    }
}
