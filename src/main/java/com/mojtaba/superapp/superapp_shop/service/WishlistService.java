package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.WishlistDto;
import java.util.List;

public interface WishlistService {
    List<WishlistDto> getWishlistItems(Long userId);
    void addProductToWishlist(Long userId, Integer productId);
    void removeProductFromWishlist(Long userId, Integer productId);
}

