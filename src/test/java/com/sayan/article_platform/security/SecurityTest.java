package com.sayan.article_platform.security;

import com.sayan.article_platform.security.SecurityConfig;
import com.sayan.article_platform.security.JwtUtil;
import com.sayan.article_platform.service.JWTUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {

    @Test
    void contextLoads() {
        JwtUtil util = new JwtUtil();
        JWTUserDetailsService userDetailsService = new JWTUserDetailsService(null, null);
        SecurityConfig config = new SecurityConfig(util, userDetailsService);

        assertNotNull(config);
    }
}