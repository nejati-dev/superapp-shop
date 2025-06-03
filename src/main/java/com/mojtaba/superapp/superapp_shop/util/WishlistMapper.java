package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.WishlistDto;
import com.mojtaba.superapp.superapp_shop.entity.Wishlist;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistDto toDto(Wishlist w) {
        WishlistDto dto = new WishlistDto();
        dto.setWishlistId(w.getWishlistId());
        dto.setProductId(w.getProduct().getProductId());
        dto.setProductName(w.getProduct().getSku());
        dto.setProductPrice(w.getProduct().getPrice());
        dto.setAddedAt(w.getAddedAt());
        return dto;
    }
}

