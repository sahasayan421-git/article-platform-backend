package com.sayan.article_platform.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CommentTreeResponse {
    private UUID id;
    private String content;
    private AuthorDto author;
    private LocalDateTime createdAt;
    private List<CommentTreeResponse> replies;
}