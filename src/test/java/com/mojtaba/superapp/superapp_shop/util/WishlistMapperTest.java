package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.WishlistDto;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.entity.Wishlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class WishlistMapperTest {

    private WishlistMapper mapper;
    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        mapper = new WishlistMapper();

        // یک User نمونه
        user = new User();
        user.setUserId(55L);

        // یک Product نمونه
        product = new Product();
        product.setProductId(200);
        product.setSku("SKU-WISH");
        product.setPrice(new BigDecimal("75.00"));
        product.setCreatedAt(Instant.parse("2023-03-01T00:00:00Z"));
        product.setUpdatedAt(Instant.parse("2023-03-02T00:00:00Z"));
        product.setCategory(null);
    }

    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        // ساخت یک Wishlist نمونه با product معتبر
        Wishlist wish = new Wishlist();
        wish.setWishlistId(15L);
        wish.setUser(user);
        wish.setProduct(product);
        wish.setAddedAt(Instant.parse("2024-01-01T08:00:00Z"));

        // فراخوانی mapper
        WishlistDto dto = mapper.toDto(wish);

        // بررسی خروجی
        assertThat(dto).isNotNull();
        assertThat(dto.getWishlistId()).isEqualTo(15L);
        assertThat(dto.getProductId()).isEqualTo(200);
        // چون در Mapper از product.getSku() استفاده می‌شود
        assertThat(dto.getProductName()).isEqualTo("SKU-WISH");
        assertThat(dto.getProductPrice()).isEqualByComparingTo(new BigDecimal("75.00"));
        assertThat(dto.getAddedAt()).isEqualTo(Instant.parse("2024-01-01T08:00:00Z"));
    }
}
