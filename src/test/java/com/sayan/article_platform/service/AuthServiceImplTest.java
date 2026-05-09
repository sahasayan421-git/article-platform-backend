package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.request.LoginRequest;
import com.sayan.article_platform.dto.request.RegisterRequest;
import com.sayan.article_platform.dto.response.AuthResponse;
import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.repository.UserRepository;
import com.sayan.article_platform.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest("Sayan", "sayan@test.com", "password123");
        loginRequest = new LoginRequest("sayan@test.com", "password123");
    }

    @Test
    void shouldLoginSuccessfully() {

        UUID userId = UUID.randomUUID();

        User user = new User();

        user.setId(userId);
        user.setUsername("Sayan");
        user.setPasswordHash("encoded-password");

        when(userRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                loginRequest.password(),
                user.getPasswordHash()
        )).thenReturn(true);

        when(jwtUtil.generate(userId))
                .thenReturn("jwt-login-token");

        AuthResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();

        assertThat(response.accessToken())
                .isEqualTo("jwt-login-token");
    }

    @Test
    void shouldThrowExceptionWhenLoginUserNameNotFound() {

        when(userRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> authService.login(loginRequest)
                );

        assertThat(exception.getMessage())
                .isEqualTo("No value present");
    }

    @Test
    void shouldThrowExceptionWhenPasswordInvalid() {

        User user = new User();

        user.setId(UUID.randomUUID());
        user.setPasswordHash("encoded-password");

        when(userRepository.findByUsername(loginRequest.username()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                loginRequest.password(),
                user.getPasswordHash()
        )).thenReturn(false);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> authService.login(loginRequest)
                );

        assertThat(exception.getMessage())
                .isEqualTo("Invalid credentials");
    }
}