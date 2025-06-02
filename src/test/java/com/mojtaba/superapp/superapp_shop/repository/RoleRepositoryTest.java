package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("🟢 وقتی یک نقش ذخیره می‌کنیم، باید قابل بازیابی با findById باشد")
    void saveAndFindById() {
        // ایجاد یک نقش جدید
        Role r = new Role();
        r.setRoleName("ADMIN");

        // ذخیره در دیتابیس درون‌خطی
        Role saved = roleRepository.save(r);

        // بازیابی با شناسه
        Optional<Role> found = roleRepository.findById(saved.getRoleId());
        assertThat(found).isPresent();
        assertThat(found.get().getRoleName()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("🟢 findByRoleName باید نقش متناظر را برگرداند")
    void findByRoleName() {
        Role r1 = new Role();
        r1.setRoleName("USER");
        roleRepository.save(r1);

        Optional<Role> found = roleRepository.findByRoleName("USER");
        assertThat(found).isPresent();
        assertThat(found.get().getRoleName()).isEqualTo("USER");
    }
}

