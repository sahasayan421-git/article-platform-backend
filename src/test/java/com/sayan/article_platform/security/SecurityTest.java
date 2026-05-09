package com.sayan.article_platform.security;

import com.sayan.article_platform.security.SecurityConfig;
import com.sayan.article_platform.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {

    @Test
    void contextLoads() {
        JwtUtil util = new JwtUtil();
        SecurityConfig config = new SecurityConfig(util);

        assertNotNull(config);
    }
}