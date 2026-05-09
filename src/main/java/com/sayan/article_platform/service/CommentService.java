package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.request.CreateCommentRequest;
import com.sayan.article_platform.dto.response.CommentTreeResponse;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    UUID reply(CreateCommentRequest request, UUID userId);

    List<CommentTreeResponse> getCommentsTree(UUID articleId);
}