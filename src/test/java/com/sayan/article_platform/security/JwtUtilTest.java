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

        String token = jwtUtil.generate(userId);

        UUID extractedUserId = jwtUtil.extract(token);

        assertThat(extractedUserId)
                .isEqualTo(userId);
    }

    @Test
    void shouldGenerateNonEmptyToken() {

        UUID userId = UUID.randomUUID();

        String token = jwtUtil.generate(userId);

        assertThat(token)
                .isNotBlank();
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {

        String invalidToken = "invalid.jwt.token";

        assertThrows(
                Exception.class,
                () -> jwtUtil.extract(invalidToken)
        );
    }

    @Test
    void shouldGenerateDifferentTokensForDifferentUsers() {

        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();

        String token1 = jwtUtil.generate(user1);
        String token2 = jwtUtil.generate(user2);

        assertThat(token1)
                .isNotEqualTo(token2);
    }

    @Test
    void shouldExtractCorrectUserIdFromGeneratedToken() {

        UUID expectedUserId = UUID.randomUUID();

        String token = jwtUtil.generate(expectedUserId);

        UUID actualUserId = jwtUtil.extract(token);

        assertThat(actualUserId)
                .isEqualTo(expectedUserId);
    }
}