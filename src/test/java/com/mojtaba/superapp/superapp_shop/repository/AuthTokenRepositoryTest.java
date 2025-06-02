package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.AuthToken;
import com.mojtaba.superapp.superapp_shop.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AuthTokenRepositoryTest {

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("🟢 وقتی یک توکن ذخیره می‌کنیم، باید با findById قابل بازیابی باشد")
    void saveAndFindById() {
        // ابتدا یک User بساز و ذخیره کن
        User user = new User();
        user.setEmail("test@example.com");
        user.setPhone("+93700123456");
        user.setPasswordHash("hashed");
        user.setPreferredLang("en");
        User savedUser = userRepository.save(user);

        // حالا یک AuthToken بساز
        AuthToken token = new AuthToken();
        token.setUser(savedUser);
        token.setToken("token123");
        token.setTokenType("access");
        token.setScope("read write");
        token.setIssuedAt(Instant.now());
        token.setExpiresAt(Instant.now().plusSeconds(3600));
        token.setRevoked(false);

        AuthToken saved = authTokenRepository.save(token);

        // بازیابی با شناسه
        Optional<AuthToken> found = authTokenRepository.findById(saved.getTokenId());
        assertThat(found).isPresent();
        AuthToken fetched = found.get();
        assertThat(fetched.getToken()).isEqualTo("token123");
        assertThat(fetched.getTokenType()).isEqualTo("access");
        assertThat(fetched.isRevoked()).isFalse();
        assertThat(fetched.getUser().getUserId()).isEqualTo(savedUser.getUserId());
    }

    @Test
    @DisplayName("🟢 findByToken باید توکن متناظر را برگرداند")
    void findByToken() {
        User user = new User();
        user.setEmail("foo@example.com");
        user.setPhone("+93700987654");
        user.setPasswordHash("pass");
        user.setPreferredLang("en");
        User savedUser = userRepository.save(user);

        AuthToken token = new AuthToken();
        token.setUser(savedUser);
        token.setToken("unique-token");
        token.setTokenType("refresh");
        token.setScope("all");
        token.setIssuedAt(Instant.now());
        token.setExpiresAt(Instant.now().plusSeconds(7200));
        token.setRevoked(false);
        authTokenRepository.save(token);

        Optional<AuthToken> found = authTokenRepository.findByToken("unique-token");
        assertThat(found).isPresent();
        assertThat(found.get().getTokenType()).isEqualTo("refresh");
        assertThat(found.get().getUser().getUserId()).isEqualTo(savedUser.getUserId());
    }

    @Test
    @DisplayName("🟢 findByUserUserId باید همهٔ توکن‌های آن کاربر را برگرداند")
    void findByUserUserId() {
        User user1 = new User();
        user1.setEmail("a@example.com");
        user1.setPhone("+93700111111");
        user1.setPasswordHash("p1");
        user1.setPreferredLang("en");
        User savedUser1 = userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("b@example.com");
        user2.setPhone("+93700222222");
        user2.setPasswordHash("p2");
        user2.setPreferredLang("en");
        User savedUser2 = userRepository.save(user2);

        // دو توکن برای user1
        AuthToken t1 = new AuthToken();
        t1.setUser(savedUser1);
        t1.setToken("t1");
        t1.setTokenType("access");
        t1.setScope("scope1");
        t1.setIssuedAt(Instant.now());
        t1.setExpiresAt(Instant.now().plusSeconds(3600));
        t1.setRevoked(false);
        authTokenRepository.save(t1);

        AuthToken t2 = new AuthToken();
        t2.setUser(savedUser1);
        t2.setToken("t2");
        t2.setTokenType("refresh");
        t2.setScope("scope2");
        t2.setIssuedAt(Instant.now());
        t2.setExpiresAt(Instant.now().plusSeconds(3600));
        t2.setRevoked(false);
        authTokenRepository.save(t2);

        // یک توکن برای user2
        AuthToken t3 = new AuthToken();
        t3.setUser(savedUser2);
        t3.setToken("t3");
        t3.setTokenType("access");
        t3.setScope("scope3");
        t3.setIssuedAt(Instant.now());
        t3.setExpiresAt(Instant.now().plusSeconds(3600));
        t3.setRevoked(false);
        authTokenRepository.save(t3);

        // بازیابی توکن‌های user1
        List<AuthToken> list1 = authTokenRepository.findByUserUserId(savedUser1.getUserId());
        assertThat(list1).hasSize(2)
                .extracting(AuthToken::getToken)
                .containsExactlyInAnyOrder("t1", "t2");

        // بازیابی توکن‌های user2
        List<AuthToken> list2 = authTokenRepository.findByUserUserId(savedUser2.getUserId());
        assertThat(list2).hasSize(1)
                .extracting(AuthToken::getToken)
                .containsExactly("t3");
    }
}

