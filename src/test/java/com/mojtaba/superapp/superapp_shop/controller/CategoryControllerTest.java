package com.mojtaba.superapp.superapp_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojtaba.superapp.superapp_shop.dto.CategoryDto;
import com.mojtaba.superapp.superapp_shop.dto.CategoryTranslationDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateCategoryDto;
import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.CategoryTranslation;
import com.mojtaba.superapp.superapp_shop.entity.CategoryTranslationId;
import com.mojtaba.superapp.superapp_shop.service.CategoryService;
import com.mojtaba.superapp.superapp_shop.util.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        // فقط Controller خودمان را standalone راه‌اندازی می‌کنیم
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void createCategory_shouldReturnCreatedCategoryDto() throws Exception {
        // ───── آماده‌سازی ورودی CreateCategoryDto برای ارسال در بدنه‌ی درخواست ─────
        CategoryTranslationDto inputTransDto = new CategoryTranslationDto();
        inputTransDto.setLangCode("en");
        inputTransDto.setName("Electronics");
        inputTransDto.setDescription("Electronic gadgets");

        CreateCategoryDto createDto = new CreateCategoryDto();
        createDto.setParentId(null);
        createDto.setTranslations(Collections.singletonList(inputTransDto));

        // ───── آماده‌سازی Category entity که Mapper از DTO می‌سازد ─────
        Category entityToSave = new Category();
        // چون parentId == null است، parent در این Entity هم null خواهد بود
        entityToSave.setParent(null);
        // مقداردهی اولیه به مجموعه‌ی translations درون Entity
        entityToSave.setTranslations(Set.of(buildCategoryTranslationEntity("en", "Electronics", "Electronic gadgets", entityToSave)));

        // ───── آماده‌سازی Category entity‌ که پس از save شدن (در دیتابیس) برگردانده می‌شود ─────
        Category savedEntity = new Category();
        savedEntity.setCategoryId(1);
        savedEntity.setParent(null);
        // برای این که toDto خروجی ترجمه‌ها را هم بگیرد:
        CategoryTranslation savedTrans = buildCategoryTranslationEntity("en", "Electronics", "Electronic gadgets", savedEntity);
        savedEntity.setTranslations(Set.of(savedTrans));

        // ───── آماده‌سازی CategoryDto خروجی که Mapper بعد از save برمی‌گرداند ─────
        CategoryDto responseDto = new CategoryDto();
        responseDto.setCategoryId(1);
        responseDto.setParentCategoryId(null);

        CategoryTranslationDto outTransDto = new CategoryTranslationDto();
        outTransDto.setLangCode("en");
        outTransDto.setName("Electronics");
        outTransDto.setDescription("Electronic gadgets");
        responseDto.setTranslations(List.of(outTransDto));

        // ───── stub کردن رفتار Mapper و Service ─────
        // ۱. وقتی controller.fromCreateDto فراخوانی شود، باید entityToSave برگرداند
        when(categoryMapper.fromCreateDto(any(CreateCategoryDto.class), isNull()))
                .thenReturn(entityToSave);

        // ۲. وقتی service.createCategory(entity) فراخوانی شود، savedEntity برگردد
        when(categoryService.createCategory(any(Category.class)))
                .thenReturn(savedEntity);

        // ۳. وقتی mapper.toDto(savedEntity) فراخوانی شود، responseDto برگردد
        when(categoryMapper.toDto(any(Category.class)))
                .thenReturn(responseDto);

        // ───── فراخوانی MockMvc و بررسی نتیجه ─────
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                // بررسی این که JSON خروجی حاوی categoryId == 1 باشد
                .andExpect(jsonPath("$.categoryId").value(1))
                // بررسی قسمت translations[0].langCode == "en"
                .andExpect(jsonPath("$.translations[0].langCode").value("en"))
                // بررسی قسمت translations[0].name == "Electronics"
                .andExpect(jsonPath("$.translations[0].name").value("Electronics"));
    }

    // یک متد کمکی برای ایجاد CategoryTranslation entity با id، langCode، name و description
    private CategoryTranslation buildCategoryTranslationEntity(
            String langCode,
            String name,
            String description,
            Category parentCategory) {

        CategoryTranslationId tid = new CategoryTranslationId();
        // در این Id معمولاً فیلد categoryId بعد از persist شدن در savedEntity ست می‌شود.
        // اما برای تست، چون savedEntity.setCategoryId(1) را زدیم، tid.setCategoryId(1) را تنظیم می‌کنیم:
        tid.setCategoryId(parentCategory.getCategoryId()); // اگر هنوز صفر است، بعد از stub سرویس می‌توان فرض کرد درست می‌شود
        tid.setLangCode(langCode);

        CategoryTranslation ct = new CategoryTranslation();
        ct.setId(tid);
        ct.setCategory(parentCategory);
        ct.setName(name);
        ct.setDescription(description);
        return ct;
    }
}
