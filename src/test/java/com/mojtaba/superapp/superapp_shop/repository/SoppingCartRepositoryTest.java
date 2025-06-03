package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.ShoppingCart;
import com.mojtaba.superapp.superapp_shop.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("🛒 وقتی یک ShoppingCart را ذخیره می‌کنیم، باید با findByUserUserId قابل بازیابی باشد")
    void saveAndFindByUserId() {
        // ابتدا یک کاربر ذخیره می‌کنیم
        User user = new User();
        user.setEmail("test@repo.com");
        user.setPhone("+93700123456");
        user.setPasswordHash("pwd");
        user.setPreferredLang("en");
        User persistedUser = userRepository.save(user);
        entityManager.flush();

        // سپس سبدی به آن کاربر اختصاص می‌دهیم
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(persistedUser);
        ShoppingCart savedCart = cartRepository.save(cart);
        entityManager.flush();

        // جستجو بر اساس شناسه‌ی کاربر
        Optional<ShoppingCart> found = cartRepository.findByUserUserId(persistedUser.getUserId());
        assertThat(found).isPresent();
        assertThat(found.get().getCartId()).isEqualTo(savedCart.getCartId());
    }
}

