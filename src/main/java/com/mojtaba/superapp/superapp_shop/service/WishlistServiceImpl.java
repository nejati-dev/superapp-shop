package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.WishlistDto;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.entity.Wishlist;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.ProductRepository;
import com.mojtaba.superapp.superapp_shop.repository.UserRepository;
import com.mojtaba.superapp.superapp_shop.repository.WishlistRepository;
import com.mojtaba.superapp.superapp_shop.util.WishlistMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final WishlistMapper wishlistMapper;

    public WishlistServiceImpl(
            WishlistRepository wishlistRepo,
            UserRepository userRepo,
            ProductRepository productRepo,
            WishlistMapper wishlistMapper
    ) {
        this.wishlistRepo = wishlistRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.wishlistMapper = wishlistMapper;
    }

    @Override
    public List<WishlistDto> getWishlistItems(Long userId) {
        userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return wishlistRepo.findByUserUserId(userId)
                .stream()
                .map(wishlistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addProductToWishlist(Long userId, Integer productId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // اگر قبلاً وجود دارد، هیچ کاری نمی‌کند (یا می‌توان خطا داد)
        boolean exists = wishlistRepo.findByUserUserIdAndProductProductId(userId, productId).isPresent();
        if (exists) {
            return; // می‌توان throw new IllegalStateException("Already in wishlist") هم کرد
        }

        Wishlist w = new Wishlist();
        w.setUser(user);
        w.setProduct(product);
        wishlistRepo.save(w);
    }

    @Override
    public void removeProductFromWishlist(Long userId, Integer productId) {
        Wishlist w = wishlistRepo.findByUserUserIdAndProductProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "combination user_id & product_id", userId + "-" + productId));
        wishlistRepo.delete(w);
    }
}

