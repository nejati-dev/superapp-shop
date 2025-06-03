package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.Product;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.entity.Wishlist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class WishlistRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("❤️ وقتی Wishlist ذخیره می‌کنیم، باید با findByUserUserId بازیابی شود")
    void saveAndFindByUserId() {
        // 1. بساز یک User و ذخیره کن
        User user = new User();
        user.setEmail("wish@domain.com");
        user.setPhone("+93700111222");
        user.setPasswordHash("pw");
        user.setPreferredLang("en");
        user = userRepository.save(user);

        // 2. یک Category ذخیره کن
        Category category = new Category();
        category = categoryRepository.save(category);

        // 3. یک Product بساز (فیلدهای NotNull را پر کن)
        Product product = new Product();
        product.setCategory(category);
        product.setSku("SKU-WISH-1");
        product.setPrice(new BigDecimal("5.50"));
        // createdAt و updatedAt توسط @PrePersist ست می‌شوند
        product = productRepository.save(product);

        // 4. حالا Wishlist جدید برای user و product بساز
        Wishlist wish = new Wishlist();
        wish.setUser(user);
        wish.setProduct(product);
        // addedAt توسط @CreationTimestamp ست می‌شود
        Wishlist saved = wishlistRepository.save(wish);

        // flush برای اطمینان از نوشتن در دیتابیس
        entityManager.flush();

        // 5. توسط findByUserUserId باید برگردد
        List<Wishlist> found = wishlistRepository.findByUserUserId(user.getUserId());
        assertThat(found).hasSize(1);
        Wishlist fetched = found.get(0);
        assertThat(fetched.getWishlistId()).isEqualTo(saved.getWishlistId());
        assertThat(fetched.getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(fetched.getProduct().getProductId()).isEqualTo(product.getProductId());
        assertThat(fetched.getAddedAt()).isNotNull();
    }

    @Test
    @DisplayName("🔍 وقتی Wishlist ذخیره می‌شود، findByUserUserIdAndProductProductId باید بازیابی کند")
    void findByUserIdAndProductId() {
        // همان مراحل قبلی؛ اما دو رکورد متفاوت برای یک user و product بسازیم
        User user = new User();
        user.setEmail("find@domain.com");
        user.setPhone("+93700133444");
        user.setPasswordHash("pw");
        user.setPreferredLang("fa");
        user = userRepository.save(user);

        Category cat = new Category();
        cat = categoryRepository.save(cat);

        Product prodA = new Product();
        prodA.setCategory(cat);
        prodA.setSku("SKU-PRODA");
        prodA.setPrice(new BigDecimal("7.00"));
        prodA = productRepository.save(prodA);

        Product prodB = new Product();
        prodB.setCategory(cat);
        prodB.setSku("SKU-PRODB");
        prodB.setPrice(new BigDecimal("8.00"));
        prodB = productRepository.save(prodB);

        // کاربر برای prodA و prodB در wishlist می‌نویسد
        Wishlist wA = new Wishlist();
        wA.setUser(user);
        wA.setProduct(prodA);
        wishlistRepository.save(wA);

        Wishlist wB = new Wishlist();
        wB.setUser(user);
        wB.setProduct(prodB);
        wishlistRepository.save(wB);

        entityManager.flush();

        // 6. findByUserUserIdAndProductProductId برای prodA
        Optional<Wishlist> maybeA = wishlistRepository
                .findByUserUserIdAndProductProductId(user.getUserId(), prodA.getProductId());
        assertThat(maybeA).isPresent();
        assertThat(maybeA.get().getProduct().getProductId()).isEqualTo(prodA.getProductId());

        // 7. و برای prodB
        Optional<Wishlist> maybeB = wishlistRepository
                .findByUserUserIdAndProductProductId(user.getUserId(), prodB.getProductId());
        assertThat(maybeB).isPresent();
        assertThat(maybeB.get().getProduct().getProductId()).isEqualTo(prodB.getProductId());
    }
}
