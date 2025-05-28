package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category createCategory(Category category);
    Optional<Category> getCategoryById(Integer id);
    List<Category> getAllCategories();
    Category updateCategory(Integer id, Category details);
    void deleteCategory(Integer id);
}
