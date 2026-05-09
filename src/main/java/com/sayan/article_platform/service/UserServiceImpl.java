package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.response.UserMentionResponse;
import com.sayan.article_platform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserMentionResponse> searchUsers(String query) {

        return repository
                .findTop10ByUsernameContainingIgnoreCase(query)
                .stream()
                .map(user -> new UserMentionResponse(
                        user.getId(),
                        user.getUsername()
                ))
                .toList();
    }
}
