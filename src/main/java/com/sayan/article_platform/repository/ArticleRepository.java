package com.sayan.article_platform.repository;

import com.sayan.article_platform.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    List<Article> findByIsPublishedTrueOrderByCreatedAtDesc();

    Page<Article> findByAuthorIdAndIsPublishedFalseOrderByCreatedAtDesc(
            UUID authorId,
            Pageable pageable
    );
}