package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Order;
import com.mojtaba.superapp.superapp_shop.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUser_UserId should return only orders of that user")
    public void testFindByUserUserId() {
        // آماده‌سازی کاربر
        User user1 = new User();
        user1.setName("Ali");
        user1.setEmail("ali@example.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Sara");
        user2.setEmail("sara@example.com");
        userRepository.save(user2);

        // درجِ دو سفارش برای user1
        Order o1 = new Order();
        o1.setUser(user1);
        o1.setStatus(Order.OrderStatus.pending);
        o1.setTotalAmount(BigDecimal.valueOf(100));
        o1.setCreatedAt(OffsetDateTime.now());
        o1.setUpdatedAt(OffsetDateTime.now());
        orderRepository.save(o1);

        Order o2 = new Order();
        o2.setUser(user1);
        o2.setStatus(Order.OrderStatus.pending);
        o2.setTotalAmount(BigDecimal.valueOf(200));
        o2.setCreatedAt(OffsetDateTime.now());
        o2.setUpdatedAt(OffsetDateTime.now());
        orderRepository.save(o2);

        // یک سفارش برای user2
        Order o3 = new Order();
        o3.setUser(user2);
        o3.setStatus(Order.OrderStatus.pending);
        o3.setTotalAmount(BigDecimal.valueOf(300));
        o3.setCreatedAt(OffsetDateTime.now());
        o3.setUpdatedAt(OffsetDateTime.now());
        orderRepository.save(o3);

        // فراخوانی متد مورد آزمون
        List<Order> results = orderRepository.findByUser_UserId(user1.getUserId());

        // ارزیابی
        assertThat(results).hasSize(2)
                .extracting(Order::getUser)
                .allMatch(u -> u.getUserId().equals(user1.getUserId()));
    }
}
