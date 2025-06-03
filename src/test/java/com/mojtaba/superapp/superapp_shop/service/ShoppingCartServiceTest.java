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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository cartRepo;
    @Mock
    private CartItemRepository itemRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private ProductRepository productRepo;
    @Mock
    private CartItemMapper itemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl svc;

    private User user;
    private Product product;
    private ShoppingCart cart;
    private CartItem existingItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setEmail("svc@user.com");
        user.setPhone("+93700123459");
        user.setPasswordHash("pwd");
        user.setPreferredLang("en");

        product = new Product();
        product.setProductId(100);
        product.setSku("SKU100");
        product.setPrice(new BigDecimal("50.00"));
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        product.setCategory(null);

        cart = new ShoppingCart();
        cart.setCartId(10L);
        cart.setUser(user);

        existingItem = new CartItem();
        existingItem.setCartItemId(5L);
        existingItem.setCart(cart);
        existingItem.setProduct(product);
        existingItem.setQuantity(2);
        existingItem.setAddedAt(Instant.now());
    }

    @Test
    void getCartItems_existingCart_returnsDtoList() {
        when(cartRepo.findByUserUserId(1L)).thenReturn(Optional.of(cart));
        when(itemRepo.findByCartCartId(10L)).thenReturn(List.of(existingItem));
        CartItemDto dto = new CartItemDto();
        dto.setCartItemId(5L);
        dto.setProductId(100);
        dto.setProductName("SKU100");
        dto.setProductPrice(new BigDecimal("50.00"));
        dto.setQuantity(2);
        dto.setAddedAt(existingItem.getAddedAt());
        when(itemMapper.toDto(existingItem)).thenReturn(dto);

        List<CartItemDto> result = svc.getCartItems(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(100);

        verify(cartRepo).findByUserUserId(1L);
        verify(itemRepo).findByCartCartId(10L);
    }

    @Test
    void getCartItems_noCart_throwsException() {
        when(cartRepo.findByUserUserId(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.getCartItems(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ShoppingCart not found");
    }

    @Test
    void addItemToCart_newCartAndNewItem_createsCartAndItem() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findById(100)).thenReturn(Optional.of(product));
        // شبیه‌سازی اینکه سبد وجود ندارد
        when(cartRepo.findByUserUserId(1L)).thenReturn(Optional.empty());
        // وقتی سبد جدید در save ذخیره می‌شود، id تولید می‌شود
        doAnswer(invocation -> {
            ShoppingCart arg = invocation.getArgument(0);
            arg.setCartId(20L);
            return arg;
        }).when(cartRepo).save(any(ShoppingCart.class));

        // آیتم از قبل وجود ندارد
        when(itemRepo.findByCartCartIdAndProductProductId(20L, 100))
                .thenReturn(Optional.empty());
        // شبیه‌سازی ذخیره‌ی آیتم
        doAnswer(invocation -> {
            CartItem arg = invocation.getArgument(0);
            arg.setCartItemId(30L);
            return arg;
        }).when(itemRepo).save(any(CartItem.class));

        // فراخوانی متد
        svc.addItemToCart(1L, 100, 3);

        // بررسی فراخوانی‌ها
        verify(userRepo).findById(1L);
        verify(productRepo).findById(100);
        verify(cartRepo).save(any(ShoppingCart.class));
        verify(itemRepo).save(any(CartItem.class));
    }

    @Test
    void addItemToCart_existingCartAndItem_updatesQuantity() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findById(100)).thenReturn(Optional.of(product));
        when(cartRepo.findByUserUserId(1L)).thenReturn(Optional.of(cart));
        when(itemRepo.findByCartCartIdAndProductProductId(10L, 100))
                .thenReturn(Optional.of(existingItem));
        when(itemRepo.save(existingItem)).thenReturn(existingItem);

        svc.addItemToCart(1L, 100, 5);

        // مقدار جدید برابر 2 + 5 = 7 باید بشود
        assertThat(existingItem.getQuantity()).isEqualTo(7);
        verify(itemRepo).save(existingItem);
    }

    @Test
    void addItemToCart_invalidQuantity_throwsException() {
        assertThatThrownBy(() -> svc.addItemToCart(1L, 100, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> svc.addItemToCart(1L, 100, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeItemFromCart_existingItem_deletesItem() {
        when(cartRepo.findByUserUserId(1L)).thenReturn(Optional.of(cart));
        when(itemRepo.findByCartCartIdAndProductProductId(10L, 100))
                .thenReturn(Optional.of(existingItem));

        svc.removeItemFromCart(1L, 100);
        verify(itemRepo).delete(existingItem);
    }

    @Test
    void removeItemFromCart_noCart_throwsException() {
        when(cartRepo.findByUserUserId(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.removeItemFromCart(1L, 100))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeItemFromCart_noItem_throwsException() {
        when(cartRepo.findByUserUserId(1L)).thenReturn(Optional.of(cart));
        when(itemRepo.findByCartCartIdAndProductProductId(10L, 100))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.removeItemFromCart(1L, 100))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void clearCart_existingCart_deletesAllItems() {
        CartItem item1 = new CartItem();
        item1.setCartItemId(1L);
        CartItem item2 = new CartItem();
        item2.setCartItemId(2L);

        when(cartRepo.findByUserUserId(1L)).thenReturn(Optional.of(cart));
        when(itemRepo.findByCartCartId(10L)).thenReturn(List.of(item1, item2));

        svc.clearCart(1L);
        verify(itemRepo).delete(item1);
        verify(itemRepo).delete(item2);
    }

    @Test
    void clearCart_noCart_throwsException() {
        when(cartRepo.findByUserUserId(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.clearCart(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

