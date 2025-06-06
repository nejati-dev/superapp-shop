package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductProductId(Integer productId);
}
