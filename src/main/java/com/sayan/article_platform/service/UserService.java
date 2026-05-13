package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.response.UserMentionResponse;
import com.sayan.article_platform.entity.User;

import java.util.List;

public interface UserService {

    List<UserMentionResponse> searchUsers(String query);

    User fetchUserById(java.util.UUID id);
}
