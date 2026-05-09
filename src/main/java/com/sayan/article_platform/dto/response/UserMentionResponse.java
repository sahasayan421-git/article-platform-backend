package com.sayan.article_platform.dto.response;

import java.util.UUID;

public record UserMentionResponse(
        UUID id,
        String username
) {}