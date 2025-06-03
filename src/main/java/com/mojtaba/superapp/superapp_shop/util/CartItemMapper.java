package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.CartItemDto;
import com.mojtaba.superapp.superapp_shop.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItemDto toDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setCartItemId(item.getCartItemId());
        dto.setProductId(item.getProduct().getProductId());
        dto.setProductName(item.getProduct().getSku());
        dto.setProductPrice(item.getProduct().getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setAddedAt(item.getAddedAt());
        return dto;
    }
}

