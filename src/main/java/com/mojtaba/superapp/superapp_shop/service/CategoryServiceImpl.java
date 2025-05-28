package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Category createCategory(Category category) {
        return repo.save(category);
    }

    @Override
    public Optional<Category> getCategoryById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return repo.findAll();
    }

    @Override
    public Category updateCategory(Integer id, Category details) {
        Category category = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with: ", "id", id));
        category.setParent(details.getParent());
        // منطق آپدیت ترجم‌ها باید اینجا اضافه بشه
        return repo.save(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with: ", "id", id);
        }
        repo.deleteById(id);
    }
}
