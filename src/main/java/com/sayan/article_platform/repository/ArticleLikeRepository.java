package com.sayan.article_platform.repository;

import com.sayan.article_platform.entity.ArticleLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, UUID> {

    long countByArticleId(UUID articleId);

    List<ArticleLike> findByArticleId(UUID articleId);

    Page<ArticleLike> findByArticleIdOrderByIdDesc(UUID articleId, Pageable pageable);
}