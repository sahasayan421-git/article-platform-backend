package com.sayan.article_platform.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    private MockHttpServletRequest request;

    private HttpServletResponse response;

    @BeforeEach
    void setUp() {

        request = new MockHttpServletRequest();

        response = new org.springframework.mock.web.MockHttpServletResponse();

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateUserWhenValidTokenProvided()
            throws ServletException, IOException {

        UUID userId = UUID.randomUUID();

        String token = "valid-jwt-token";

        request.addHeader(
                "Authorization",
                "Bearer " + token
        );

        when(jwtUtil.extract(token))
                .thenReturn(userId);

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        Object principal =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        assertThat(principal)
                .isEqualTo(userId);

        verify(filterChain)
                .doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenAuthorizationHeaderMissing()
            throws ServletException, IOException {

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertThat(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        ).isNull();

        verify(filterChain)
                .doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenHeaderDoesNotStartWithBearer()
            throws ServletException, IOException {

        request.addHeader(
                "Authorization",
                "Basic abc123"
        );

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertThat(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        ).isNull();

        verify(filterChain)
                .doFilter(request, response);
    }

    @Test
    void shouldThrowExceptionForInvalidToken()
            throws ServletException, IOException {

        String token = "invalid-token";

        request.addHeader(
                "Authorization",
                "Bearer " + token
        );

        when(jwtUtil.extract(token))
                .thenThrow(new RuntimeException("Invalid token"));

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> jwtFilter.doFilterInternal(
                        request,
                        response,
                        filterChain
                )
        );
    }

    @Test
    void shouldOverwriteExistingAuthentication()
            throws ServletException, IOException {

        UUID userId = UUID.randomUUID();

        String token = "new-token";

        request.addHeader(
                "Authorization",
                "Bearer " + token
        );

        when(jwtUtil.extract(token))
                .thenReturn(userId);

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertThat(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
        ).isEqualTo(userId);
    }
}