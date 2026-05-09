package com.sayan.article_platform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LikesCountResponse {
    private UUID articleId;
    private long likesCount;
}