// src/test/java/com/mojtaba/superapp/superapp_shop/repository/ProductRepositoryTest.java
package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("🟢 وقتی یک محصول ذخیره می‌کنیم، باید بتوان آن را با findBySku یافت")
    void saveAndFindBySku() {
        // ابتدا یک Category برای رابطه تنظیم می‌کنیم
        Category cat = new Category();
        Category savedCat = categoryRepository.save(cat);

        // ایجاد و ذخیرهٔ محصول
        Product p = new Product();
        p.setCategory(savedCat);
        p.setSku("SKU-123");
        p.setPrice(new BigDecimal("49.99"));
        // createdAt و updatedAt توسط @PrePersist تنظیم می‌شوند
        Product saved = productRepository.save(p);

        Optional<Product> found = productRepository.findBySku("SKU-123");
        assertThat(found).isPresent();
        assertThat(found.get().getProductId()).isEqualTo(saved.getProductId());
        assertThat(found.get().getCategory().getCategoryId()).isEqualTo(savedCat.getCategoryId());
    }

    @Test
    @DisplayName("🔴 findBySku وقتی محصول وجود نداشته باشد باید Optional.empty برگشت دهد")
    void findBySku_nonExisting_returnsEmpty() {
        Optional<Product> found = productRepository.findBySku("NON-EXISTENT");
        assertThat(found).isEmpty();
    }
}

