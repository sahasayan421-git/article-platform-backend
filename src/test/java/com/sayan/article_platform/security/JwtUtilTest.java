package com.sayan.article_platform.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void shouldGenerateAndExtractUserIdSuccessfully() {

        UUID userId = UUID.randomUUID();

        String userName = "sayan";
        String token = jwtUtil.generate(userName);

        String extractedUserId = jwtUtil.extractUserName(token);

        assertThat(extractedUserId)
                .isEqualTo("sayan");
    }

    @Test
    void shouldGenerateNonEmptyToken() {

        UUID userId = UUID.randomUUID();

        String userName = "sayan";
        String token = jwtUtil.generate(userName);

        assertThat(token)
                .isNotBlank();
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {

        String invalidToken = "invalid.jwt.token";

        assertThrows(
                Exception.class,
                () -> jwtUtil.extractUserName(invalidToken)
        );
    }

    @Test
    void shouldGenerateDifferentTokensForDifferentUsers() {

        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();

        String userName = "sayan";
        String token = jwtUtil.generate(userName);
        String userName1 = "saha";
        String token1 = jwtUtil.generate(userName1);

        assertThat(token1)
                .isNotEqualTo(token);
    }

    @Test
    void shouldExtractCorrectUserIdFromGeneratedToken() {

        UUID expectedUserId = UUID.randomUUID();

        String token = jwtUtil.generate("testuser");

        String actualUserId = jwtUtil.extractUserName(token);

        assertThat(actualUserId)
                .isEqualTo("testuser");
    }
}