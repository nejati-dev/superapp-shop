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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceImplTest {

    @Mock private WishlistRepository wishlistRepo;
    @Mock private UserRepository userRepo;
    @Mock private ProductRepository productRepo;
    @Mock private WishlistMapper wishlistMapper;

    @InjectMocks
    private WishlistServiceImpl svc;

    private User user;
    private Product product;
    private Wishlist existingW;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setEmail("wish@user.com");
        user.setPhone("+93700123460");
        user.setPasswordHash("pwd");
        user.setPreferredLang("fa");

        product = new Product();
        product.setProductId(200);
        product.setSku("SKU200");
        product.setPrice(new BigDecimal("75.00"));
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        product.setCategory(null);

        existingW = new Wishlist();
        existingW.setWishlistId(15L);
        existingW.setUser(user);
        existingW.setProduct(product);
        existingW.setAddedAt(Instant.now());
    }

    @Test
    void getWishlistItems_existingUser_returnsDtoList() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(wishlistRepo.findByUserUserId(1L)).thenReturn(List.of(existingW));

        WishlistDto dto = new WishlistDto();
        dto.setWishlistId(15L);
        dto.setProductId(200);
        dto.setProductName("SKU200");
        dto.setProductPrice(new BigDecimal("75.00"));
        dto.setAddedAt(existingW.getAddedAt());
        when(wishlistMapper.toDto(existingW)).thenReturn(dto);

        List<WishlistDto> result = svc.getWishlistItems(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(200);

        verify(userRepo).findById(1L);
        verify(wishlistRepo).findByUserUserId(1L);
    }

    @Test
    void getWishlistItems_noUser_throwsException() {
        when(userRepo.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.getWishlistItems(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addProductToWishlist_successfulAdd() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findById(200)).thenReturn(Optional.of(product));
        when(wishlistRepo.findByUserUserIdAndProductProductId(1L, 200)).thenReturn(Optional.empty());
        Wishlist newW = new Wishlist();
        newW.setWishlistId(100L);
        newW.setUser(user);
        newW.setProduct(product);
        when(wishlistRepo.save(any(Wishlist.class))).thenReturn(newW);

        // نباید خطا دهد
        svc.addProductToWishlist(1L, 200);
        verify(wishlistRepo).save(any(Wishlist.class));
    }

    @Test
    void addProductToWishlist_alreadyExists_noop() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findById(200)).thenReturn(Optional.of(product));
        when(wishlistRepo.findByUserUserIdAndProductProductId(1L, 200))
                .thenReturn(Optional.of(existingW));

        // باید بدون خطا اجرا شود و save هیچ‌وقت فراخوانی نشود
        svc.addProductToWishlist(1L, 200);
        verify(wishlistRepo, never()).save(any(Wishlist.class));
    }

    @Test
    void addProductToWishlist_noUser_throwsException() {
        when(userRepo.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.addProductToWishlist(2L, 200))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addProductToWishlist_noProduct_throwsException() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findById(300)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.addProductToWishlist(1L, 300))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeProductFromWishlist_existing_removes() {
        when(wishlistRepo.findByUserUserIdAndProductProductId(1L, 200))
                .thenReturn(Optional.of(existingW));

        svc.removeProductFromWishlist(1L, 200);
        verify(wishlistRepo).delete(existingW);
    }

    @Test
    void removeProductFromWishlist_notExists_throwsException() {
        when(wishlistRepo.findByUserUserIdAndProductProductId(1L, 200))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.removeProductFromWishlist(1L, 200))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

