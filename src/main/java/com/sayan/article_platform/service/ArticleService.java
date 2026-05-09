package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.request.CreateArticleRequest;
import com.sayan.article_platform.dto.response.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ArticleService {

    ArticleResponse createArticle(CreateArticleRequest request);

    ArticleResponse publishArticle(UUID articleId);

    ArticleResponse editArticle(UUID articleId, CreateArticleRequest request);

    List<ArticleResponse> getPublishedArticles();

    ArticleResponse getArticleById(UUID articleId);

    Page<ArticleResponse> getMyDrafts(Pageable pageable);
}