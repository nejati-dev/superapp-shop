package com.mojtaba.superapp.superapp_shop.Repository;

import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    void whenSaveAndFindByEmail_thenReturnUser() {
        // given
        User u = new User();
        u.setEmail("test@example.com");
        u.setPasswordHash("hash");
        u.setPreferredLang("en");
        // لازم نیست نقش بذاری فعلاً
        userRepo.save(u);

        // when
        Optional<User> found = userRepo.findByEmail("test@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }
}

