package com.sayan.article_platform.repository;

import com.sayan.article_platform.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    List<User> findTop10ByUsernameContainingIgnoreCase(String query);

    Boolean existsByEmail(@NotBlank String email);
}