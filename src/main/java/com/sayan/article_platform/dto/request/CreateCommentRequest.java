package com.sayan.article_platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateCommentRequest(
        @NotNull UUID articleId,
        UUID parentCommentId,
        @NotBlank String content
) {}