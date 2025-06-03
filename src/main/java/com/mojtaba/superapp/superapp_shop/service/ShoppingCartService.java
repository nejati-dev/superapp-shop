package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.CartItemDto;
import java.util.List;

public interface ShoppingCartService {
    List<CartItemDto> getCartItems(Long userId);
    void addItemToCart(Long userId, Integer productId, Integer quantity);
    void removeItemFromCart(Long userId, Integer productId);
    void clearCart(Long userId);
}

