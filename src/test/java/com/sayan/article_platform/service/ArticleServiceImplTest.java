package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.response.ArticleResponse;
import com.sayan.article_platform.entity.Article;
import com.sayan.article_platform.repository.ArticleRepository;
import com.sayan.article_platform.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    void shouldReturnCurrentUsersDraftArticles() {

        Pageable pageable = PageRequest.of(0, 10);

        Article article = new Article();
        article.setId(UUID.randomUUID());
        article.setTitle("Draft Article");
        article.setContent("Draft Content");
        article.setIsPublished(false);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        Page<Article> articlePage = new PageImpl<>(List.of(article));

        when(authUtil.getCurrentUserId()).thenReturn(userId);

        when(articleRepository
                .findByAuthorIdAndIsPublishedFalseOrderByCreatedAtDesc(
                        userId,
                        pageable
                ))
                .thenReturn(articlePage);

        Page<ArticleResponse> response = articleService.getMyDrafts(pageable);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);

        ArticleResponse result = response.getContent().get(0);

        assertThat(result.title()).isEqualTo("Draft Article");
        assertThat(result.content()).isEqualTo("Draft Content");
        assertThat(result.published()).isFalse();

        verify(authUtil, times(2)).getCurrentUserId();

        verify(articleRepository)
                .findByAuthorIdAndIsPublishedFalseOrderByCreatedAtDesc(
                        userId,
                        pageable
                );
    }

    @Test
    void shouldReturnEmptyDraftPageWhenNoDraftsExist() {

        Pageable pageable = PageRequest.of(0, 10);

        when(authUtil.getCurrentUserId()).thenReturn(userId);

        when(articleRepository
                .findByAuthorIdAndIsPublishedFalseOrderByCreatedAtDesc(
                        userId,
                        pageable
                ))
                .thenReturn(Page.empty());

        Page<ArticleResponse> response = articleService.getMyDrafts(pageable);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEmpty();

        verify(authUtil, times(2)).getCurrentUserId();
    }
}
