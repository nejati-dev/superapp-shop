package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.dto.WishlistDto;
import com.mojtaba.superapp.superapp_shop.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * دریافت لیست علاقه‌مندی‌ها برای یک کاربر
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistDto>> getWishlistItems(@PathVariable Long userId) {
        List<WishlistDto> items = wishlistService.getWishlistItems(userId);
        return ResponseEntity.ok(items);
    }

    /**
     * افزودن محصول به لیست علاقه‌مندی
     */
    @PostMapping("/{userId}/products/{productId}")
    public ResponseEntity<Void> addProductToWishlist(
            @PathVariable Long userId,
            @PathVariable Integer productId
    ) {
        wishlistService.addProductToWishlist(userId, productId);
        return ResponseEntity.ok().build();
    }

    /**
     * حذف یک محصول از لیست علاقه‌مندی
     */
    @DeleteMapping("/{userId}/products/{productId}")
    public ResponseEntity<Void> removeProductFromWishlist(
            @PathVariable Long userId,
            @PathVariable Integer productId
    ) {
        wishlistService.removeProductFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }
}

