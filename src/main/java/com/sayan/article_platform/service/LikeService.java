package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.response.LikesCountResponse;
import com.sayan.article_platform.dto.response.UserLikeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LikeService {
    void likeArticle(UUID articleId, UUID userId);

    LikesCountResponse getLikesCount(UUID articleId);

    Page<UserLikeResponse> getUsersWhoLiked(
            UUID articleId,
            Pageable pageable);
}