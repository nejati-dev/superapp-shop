package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByParentCategoryId(Integer parentId);
}