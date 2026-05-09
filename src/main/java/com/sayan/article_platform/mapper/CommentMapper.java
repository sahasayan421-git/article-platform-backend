package com.sayan.article_platform.mapper;

import com.sayan.article_platform.dto.response.CommentResponse;
import com.sayan.article_platform.entity.Comment;

public class CommentMapper {

    public static CommentResponse toDto(Comment c) {
        return new CommentResponse(c.getId(), c.getContent());
    }
}