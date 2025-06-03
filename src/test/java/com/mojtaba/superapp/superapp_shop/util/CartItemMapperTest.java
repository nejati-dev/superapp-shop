package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.CartItemDto;
import com.mojtaba.superapp.superapp_shop.entity.CartItem;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.entity.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemMapperTest {

    private CartItemMapper mapper;
    private Product product;
    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        mapper = new CartItemMapper();

        // یک ShoppingCart نمونه (فقط برای مقداردهی فرضی در CartItem)
        cart = new ShoppingCart();
        cart.setCartId(42L);

        // یک Product نمونه جهت تست
        product = new Product();
        product.setProductId(100);
        product.setSku("SKU-TEST");
        product.setPrice(new BigDecimal("123.45"));
        product.setCreatedAt(Instant.parse("2023-01-01T00:00:00Z"));
        product.setUpdatedAt(Instant.parse("2023-01-02T00:00:00Z"));
        product.setCategory(null); // در این تست فقط sku و price استفاده می‌شوند
    }

    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        // ساختن یک CartItem نمونه که دارای یک product معتبر است
        CartItem item = new CartItem();
        item.setCartItemId(7L);
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(5);
        item.setAddedAt(Instant.parse("2024-05-10T12:34:56Z"));

        // فراخوانی mapper
        CartItemDto dto = mapper.toDto(item);

        // بررسی فیلدهای خروجی
        assertThat(dto).isNotNull();
        assertThat(dto.getCartItemId()).isEqualTo(7L);
        assertThat(dto.getProductId()).isEqualTo(100);
        // چون در CartItemMapper از product.getSku() استفاده شده
        assertThat(dto.getProductName()).isEqualTo("SKU-TEST");
        assertThat(dto.getProductPrice()).isEqualByComparingTo(new BigDecimal("123.45"));
        assertThat(dto.getQuantity()).isEqualTo(5);
        assertThat(dto.getAddedAt()).isEqualTo(Instant.parse("2024-05-10T12:34:56Z"));
    }
}
