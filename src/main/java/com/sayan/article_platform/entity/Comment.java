package com.sayan.article_platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "article_id")
    private UUID articleId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "parent_comment_id")
    private UUID parentCommentId;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}