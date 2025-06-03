// src/test/java/com/mojtaba/superapp/superapp_shop/repository/ProductImageRepositoryTest.java
package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.entity.ProductImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ProductImageRepositoryTest {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("🟢 findByProductProductId باید همه تصاویر را برگرداند")
    void findByProductProductId_returnsList() {
        // آماده‌سازی Category و Product
        Category cat = new Category();
        Category savedCat = categoryRepository.save(cat);

        Product p = new Product();
        p.setCategory(savedCat);
        p.setSku("SKU-IMG-1");
        p.setPrice(new BigDecimal("19.99"));
        Product savedProduct = productRepository.save(p);

        // دو نمونه ProductImage ذخیره می‌کنیم
        ProductImage img1 = new ProductImage();
        img1.setProduct(savedProduct);
        img1.setUrl("http://example.com/img1.jpg");
        productImageRepository.save(img1);

        ProductImage img2 = new ProductImage();
        img2.setProduct(savedProduct);
        img2.setUrl("http://example.com/img2.jpg");
        productImageRepository.save(img2);

        List<ProductImage> images = productImageRepository.findByProductProductId(savedProduct.getProductId());
        assertThat(images).hasSize(2)
                .extracting(ProductImage::getUrl)
                .containsExactlyInAnyOrder("http://example.com/img1.jpg", "http://example.com/img2.jpg");
    }

    @Test
    @DisplayName("🔴 اگر تصویری برای productId موجود نباشد، لیست خالی برمی‌گرداند")
    void findByProductProductId_noImages_returnsEmptyList() {
        List<ProductImage> images = productImageRepository.findByProductProductId(9999);
        assertThat(images).isEmpty();
    }
}

