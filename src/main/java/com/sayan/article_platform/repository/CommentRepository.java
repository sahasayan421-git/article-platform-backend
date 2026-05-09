package com.sayan.article_platform.repository;

import com.sayan.article_platform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByArticleIdOrderByCreatedAtAsc(UUID articleId);

    List<Comment> findByArticleIdOrderByCreatedAtDesc(UUID articleId);
}