package com.sayan.article_platform.dto.response;

import java.util.UUID;

public record CommentResponse(UUID id, String content) {}