package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CartItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("🛍 وقتی CartItem ذخیره می‌کنیم، باید با findByCartCartId بازیابی شود")
    void saveAndFindByCartId() {
        User user = new User();
        user.setEmail("test@domain.com");
        user.setPhone("+93700123456");
        user.setPasswordHash("pass");
        user.setPreferredLang("en");
        user = userRepository.save(user);


        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart = cartRepository.save(cart);


        Category category = new Category();
        category = categoryRepository.save(category);


        Product product = new Product();
        product.setCategory(category);
        product.setSku("SKU-ABC-123");
        product.setPrice(new BigDecimal("19.99"));
        product = productRepository.save(product);


        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(3);
        CartItem saved = cartItemRepository.save(item);


        List<CartItem> foundList = cartItemRepository.findByCartCartId(cart.getCartId());
        assertThat(foundList).hasSize(1);
        CartItem fetched = foundList.get(0);
        assertThat(fetched.getCart().getCartId()).isEqualTo(cart.getCartId());
        assertThat(fetched.getProduct().getProductId()).isEqualTo(product.getProductId());
        assertThat(fetched.getQuantity()).isEqualTo(3);
        assertThat(fetched.getAddedAt()).isNotNull();
    }

    @Test
    @DisplayName("🔍 وقتی CartItem با محصول خاص ذخیره می‌شود، findByCartCartIdAndProductProductId باید بازیابی کند")
    void findByCartIdAndProductId() {
        User user = new User();
        user.setEmail("find@domain.com");
        user.setPhone("+93700987654");
        user.setPasswordHash("pwd");
        user.setPreferredLang("en");
        user = userRepository.save(user);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart = cartRepository.save(cart);

        Category category = new Category();
        category = categoryRepository.save(category);

        Product productA = new Product();
        productA.setCategory(category);
        productA.setSku("SKU-A");
        productA.setPrice(new BigDecimal("10.00"));
        productA = productRepository.save(productA);

        Product productB = new Product();
        productB.setCategory(category);
        productB.setSku("SKU-B");
        productB.setPrice(new BigDecimal("20.00"));
        productB = productRepository.save(productB);

        CartItem itemA = new CartItem();
        itemA.setCart(cart);
        itemA.setProduct(productA);
        itemA.setQuantity(1);
        cartItemRepository.save(itemA);

        CartItem itemB = new CartItem();
        itemB.setCart(cart);
        itemB.setProduct(productB);
        itemB.setQuantity(2);
        cartItemRepository.save(itemB);


        Optional<CartItem> maybe = cartItemRepository.findByCartCartIdAndProductProductId(
                cart.getCartId(), productA.getProductId()
        );
        assertThat(maybe).isPresent();
        CartItem found = maybe.get();
        assertThat(found.getQuantity()).isEqualTo(1);

        Optional<CartItem> maybeB = cartItemRepository.findByCartCartIdAndProductProductId(
                cart.getCartId(), productB.getProductId()
        );
        assertThat(maybeB).isPresent();
        assertThat(maybeB.get().getQuantity()).isEqualTo(2);
    }
}
