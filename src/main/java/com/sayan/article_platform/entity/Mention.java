package com.sayan.article_platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mentions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mention {

    @Id
    private UUID id;

    @Column(name = "mentioned_user_id")
    private UUID mentionedUserId;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "source_id")
    private UUID sourceId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}