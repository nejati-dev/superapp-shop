package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.dto.CartItemDto;
import com.mojtaba.superapp.superapp_shop.service.ShoppingCartService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    /**
     * دریافت تمام آیتم‌های سبد برای یک کاربر
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemDto>> getCartItems(@PathVariable Long userId) {
        List<CartItemDto> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }

    /**
     * افزودن یا بروزرسانی آیتم در سبد
     */
    @PostMapping("/{userId}/items")
    public ResponseEntity<Void> addItemToCart(
            @PathVariable Long userId,
            @RequestParam Integer productId,
            @RequestParam @Min(1) Integer quantity
    ) {
        cartService.addItemToCart(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    /**
     * حذف یک آیتم از سبد
     */
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Integer productId
    ) {
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * خالی کردن کل سبد
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}

