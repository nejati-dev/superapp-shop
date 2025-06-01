package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.dto.*;
import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.CategoryService;
import com.mojtaba.superapp.superapp_shop.util.CategoryMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;
    private final CategoryMapper mapper;

    public CategoryController(CategoryService service, CategoryMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CreateCategoryDto dto) {
        Category parent = dto.getParentId() != null ?
                service.getCategoryById(dto.getParentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with", "id", dto.getParentId()))
                : null;
        Category category = mapper.fromCreateDto(dto, parent);
        Category saved = service.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> list() {
        List<CategoryDto> categories = service.getAllCategories().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getOne(@PathVariable Integer id) {
        Category category = service.getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with", "id", id));
        return ResponseEntity.ok(mapper.toDto(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Integer id, @Valid @RequestBody CreateCategoryDto dto) {
        Category category = service.getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with","id", id));
        Category parent = dto.getParentId() != null ?
                service.getCategoryById(dto.getParentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with", "id", dto.getParentId()))
                : null;
        Category updated = mapper.fromCreateDto(dto, parent);
        Category saved = service.updateCategory(id, updated);
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}