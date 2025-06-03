// src/test/java/com/mojtaba/superapp/superapp_shop/repository/ProductTranslationRepositoryTest.java
package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.entity.ProductTranslation;
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
class ProductTranslationRepositoryTest {

    @Autowired
    private ProductTranslationRepository productTranslationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("🟢 findByProductProductId باید همه ترجمه‌ها را برگرداند")
    void findByProductProductId_returnsList() {
        // آماده‌سازی Category و Product
        Category cat = new Category();
        Category savedCat = categoryRepository.save(cat);

        Product p = new Product();
        p.setCategory(savedCat);
        p.setSku("SKU-TR-1");
        p.setPrice(new BigDecimal("29.99"));
        Product savedProduct = productRepository.save(p);

        // دو ترجمهٔ محصول ذخیره می‌کنیم
        ProductTranslation t1 = new ProductTranslation();
        t1.setProduct(savedProduct);
        t1.setLangCode("en");
        t1.setName("Widget");
        t1.setDescription("A useful widget");
        productTranslationRepository.save(t1);

        ProductTranslation t2 = new ProductTranslation();
        t2.setProduct(savedProduct);
        t2.setLangCode("fa");
        t2.setName("ابزارک");
        t2.setDescription("یک ابزارک مفید");
        productTranslationRepository.save(t2);

        List<ProductTranslation> translations = productTranslationRepository.findByProductProductId(savedProduct.getProductId());
        assertThat(translations).hasSize(2)
                .extracting(ProductTranslation::getLangCode)
                .containsExactlyInAnyOrder("en", "fa");
    }

    @Test
    @DisplayName("🔴 اگر ترجمه‌ای برای productId موجود نباشد، لیست خالی برمی‌گرداند")
    void findByProductProductId_noTranslations_returnsEmptyList() {
        List<ProductTranslation> translations = productTranslationRepository.findByProductProductId(8888);
        assertThat(translations).isEmpty();
    }
}

