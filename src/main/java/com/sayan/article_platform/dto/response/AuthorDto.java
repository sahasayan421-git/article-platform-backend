package com.sayan.article_platform.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthorDto {
    private UUID id;
    private String username;
}
