package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.response.UserMentionResponse;

import java.util.List;

public interface UserService {

    List<UserMentionResponse> searchUsers(String query);
}
