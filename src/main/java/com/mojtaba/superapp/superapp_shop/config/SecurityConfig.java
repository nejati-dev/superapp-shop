package com.mojtaba.superapp.superapp_shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // می‌تونی strength رو هم پاس بدی به کانستراکتور
        return new BCryptPasswordEncoder();
    }
}
