package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.request.LoginRequest;
import com.sayan.article_platform.dto.request.RegisterRequest;
import com.sayan.article_platform.dto.response.AuthResponse;
import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.repository.UserRepository;
import com.sayan.article_platform.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
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
        User user = new User(UUID.randomUUID(), request.username(), request.email(), passwordEncoder.encode(request.password()));
        repo.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = repo.findByUsername(request.username()).orElseThrow();
        if(!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new RuntimeException("Invalid credentials");
        }
        return new AuthResponse(jwtUtil.generate(user.getId()));
    }
}