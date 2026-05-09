package com.sayan.article_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "article_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLike {

    @Id
    private UUID id;

    @Column(name = "article_id")
    private UUID articleId;

    @Column(name = "user_id")
    private UUID userId;
}