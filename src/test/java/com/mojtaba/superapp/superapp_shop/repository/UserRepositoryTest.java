package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository repo;

    @Test
    void whenSaveAndFindByEmail_thenReturnUser() {
        User user = new User();
        user.setEmail("x@x.com");
        user.setPhone("+93700123456");
        user.setPasswordHash("somehashed");
        user.setPreferredLang("en");
        repo.save(user);

        Optional<User> found = repo.findByEmail("x@x.com");
        assertThat(found).isPresent().contains(user);
    }
}


