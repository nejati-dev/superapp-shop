package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.CartItemDto;
import com.mojtaba.superapp.superapp_shop.entity.CartItem;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.entity.ShoppingCart;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.CartItemRepository;
import com.mojtaba.superapp.superapp_shop.repository.ProductRepository;
import com.mojtaba.superapp.superapp_shop.repository.ShoppingCartRepository;
import com.mojtaba.superapp.superapp_shop.repository.UserRepository;
import com.mojtaba.superapp.superapp_shop.util.CartItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final CartItemMapper itemMapper;

    public ShoppingCartServiceImpl(
            ShoppingCartRepository cartRepo,
            CartItemRepository itemRepo,
            UserRepository userRepo,
            ProductRepository productRepo,
            CartItemMapper itemMapper
    ) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.itemMapper = itemMapper;
    }

    @Override
    public List<CartItemDto> getCartItems(Long userId) {
        ShoppingCart cart = cartRepo.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart", "user_id", userId));
        return itemRepo.findByCartCartId(cart.getCartId())
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addItemToCart(Long userId, Integer productId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // اگر سبد وجود ندارد، ایجادش کن
        ShoppingCart cart = cartRepo.findByUserUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    return cartRepo.save(newCart);
                });

        // اگر آیتم از قبل وجود داشت، بروزرسانی کن
        CartItem item = itemRepo.findByCartCartIdAndProductProductId(cart.getCartId(), productId)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + quantity);
        itemRepo.save(item);
    }

    @Override
    public void removeItemFromCart(Long userId, Integer productId) {
        ShoppingCart cart = cartRepo.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart", "user_id", userId));
        CartItem item = itemRepo.findByCartCartIdAndProductProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "product_id", productId));

        itemRepo.delete(item);
    }

    @Override
    public void clearCart(Long userId) {
        ShoppingCart cart = cartRepo.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart", "user_id", userId));
        // حذف شئی‌شان
        itemRepo.findByCartCartId(cart.getCartId()).forEach(itemRepo::delete);
    }
}

