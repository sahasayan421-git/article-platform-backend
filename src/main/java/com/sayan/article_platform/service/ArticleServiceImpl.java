package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.request.CreateArticleRequest;
import com.sayan.article_platform.dto.response.ArticleResponse;
import com.sayan.article_platform.entity.Article;
import com.sayan.article_platform.repository.ArticleRepository;
import com.sayan.article_platform.security.SecurityUtil;

import com.sayan.article_platform.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository repository;
    private final AuthUtil authUtil;

    public ArticleServiceImpl(ArticleRepository repository, AuthUtil authUtil) {

        this.repository = repository;
        this.authUtil = authUtil;
    }

    // 🔒 Create Article → Evict list cache
    @Override
    @CacheEvict(value = "articles", allEntries = true)
    public ArticleResponse createArticle(CreateArticleRequest request) {

        log.info("Creating article with title: {}", request.title());

        UUID userId = SecurityUtil.getCurrentUserId();

        Article article = new Article();
        article.setAuthorId(userId);
        article.setTitle(request.title());
        article.setContent(request.content());
        article.setUpdatedAt(LocalDateTime.now());
        article.setIsPublished(false);

        return map(repository.save(article));
    }

    // 🔒 Create Article → Evict list cache
    @Override
    @CacheEvict(value = "articles", allEntries = true)
    public ArticleResponse editArticle(UUID articleId, CreateArticleRequest request) {

        log.info("Editing article with id: {}", articleId);

        UUID userId = SecurityUtil.getCurrentUserId();

        Article article = findOrThrow(articleId);
        article.setAuthorId(userId);
        article.setTitle(request.title());
        article.setContent(request.content());
        article.setUpdatedAt(LocalDateTime.now());
        article.setIsPublished(false);

        return map(repository.save(article));
    }

    // 🔒 Publish → Evict list + single article cache
    @Override
    @CacheEvict(value = {"articles", "article"}, allEntries = true)
    public ArticleResponse publishArticle(UUID articleId) {

        log.info("Publishing article with id: {}", articleId);

        UUID userId = SecurityUtil.getCurrentUserId();

        Article article = findOrThrow(articleId);

        if (!article.getAuthorId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        article.setCreatedAt(LocalDateTime.now());
        article.setIsPublished(true);


        return map(article);
    }

    // 🔓 Cached: list
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "articles", key = "'all'")
    public List<ArticleResponse> getPublishedArticles() {

        log.info("Fetching published articles");

        return repository.findByIsPublishedTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::map)
                .toList();
    }

    // 🔓 Cached: single article
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "article", key = "#articleId")
    public ArticleResponse getArticleById(UUID articleId) {

        log.info("Fetching article with id: {}", articleId);

        return map(findOrThrow(articleId));
    }

    private Article findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    private ArticleResponse map(Article a) {
        return new ArticleResponse(
                a.getId(),
                a.getTitle(),
                a.getContent(),
                a.getAuthorId(),
                a.getIsPublished(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }

    @Override
    public Page<ArticleResponse> getMyDrafts(Pageable pageable) {

        log.info("Fetching drafts for user: {}", authUtil.getCurrentUserId());

        return repository
                .findByAuthorIdAndIsPublishedFalseOrderByCreatedAtDesc(
                        authUtil.getCurrentUserId(),
                        pageable
                )
                .map(this::map);
    }
}