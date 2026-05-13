package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.request.LoginRequest;
import com.sayan.article_platform.dto.request.RegisterRequest;
import com.sayan.article_platform.dto.response.AuthResponse;
import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.repository.UserRepository;
import com.sayan.article_platform.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository repo, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterRequest request) {
        log.info("Register request received for email: {}", request.email());
        User user = new User(UUID.randomUUID(), request.username(), request.email(), passwordEncoder.encode(request.password()));
        repo.save(user);
        log.info("User registered successfully with email: {}", request.email());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.username());
        User user = repo.findByUsername(request.username()).orElseThrow();
        if(!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new RuntimeException("Invalid credentials");
        }
        log.info("Login successful for email: {}", request.username());
        return new AuthResponse(jwtUtil.generate(user.getUsername()));
    }
}