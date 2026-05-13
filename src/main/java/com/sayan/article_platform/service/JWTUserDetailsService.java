package com.sayan.article_platform.service;

import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.model.UserPrincipal;
import com.sayan.article_platform.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JWTUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserService userService;

    private final UserRepository userRepo;

    public JWTUserDetailsService(UserService userService, UserRepository userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserPrincipal(user);
    }
}
