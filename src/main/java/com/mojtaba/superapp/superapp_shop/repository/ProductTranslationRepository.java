package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.ProductTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTranslationRepository extends JpaRepository<ProductTranslation, Long> {
    List<ProductTranslation> findByProductProductId(Integer productId);
}
