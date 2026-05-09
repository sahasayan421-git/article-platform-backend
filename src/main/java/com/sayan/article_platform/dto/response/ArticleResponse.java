package com.sayan.article_platform.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ArticleResponse(
        UUID id,
        String title,
        String content,
        UUID authorId,
        boolean published,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}