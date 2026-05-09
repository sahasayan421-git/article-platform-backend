package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.response.LikesCountResponse;
import com.sayan.article_platform.dto.response.UserLikeResponse;
import com.sayan.article_platform.entity.ArticleLike;
import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.exception.ResourceNotFoundException;
import com.sayan.article_platform.repository.ArticleLikeRepository;
import com.sayan.article_platform.repository.ArticleRepository;
import com.sayan.article_platform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    @Mock
    private ArticleLikeRepository repo;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private LikeServiceImpl likeService;

    private UUID articleId;

    private UUID userId;

    @BeforeEach
    void setUp() {

        articleId = UUID.randomUUID();

        userId = UUID.randomUUID();
    }

    @Test
    void shouldLikeArticleSuccessfully() {

        likeService.likeArticle(articleId, userId);

        ArgumentCaptor<ArticleLike> captor =
                ArgumentCaptor.forClass(ArticleLike.class);

        verify(repo).save(captor.capture());

        ArticleLike savedLike = captor.getValue();

        assertThat(savedLike.getArticleId())
                .isEqualTo(articleId);

        assertThat(savedLike.getUserId())
                .isEqualTo(userId);

        assertThat(savedLike.getId())
                .isNotNull();
    }

    @Test
    void shouldReturnLikesCount() {

        when(articleRepository.existsById(articleId))
                .thenReturn(true);

        when(repo.countByArticleId(articleId))
                .thenReturn(5L);

        LikesCountResponse response =
                likeService.getLikesCount(articleId);

        assertThat(response).isNotNull();

        assertThat(response.getArticleId())
                .isEqualTo(articleId);

        assertThat(response.getLikesCount())
                .isEqualTo(5L);
    }

    @Test
    void shouldThrowExceptionWhenArticleNotFoundForCount() {

        when(articleRepository.existsById(articleId))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> likeService.getLikesCount(articleId)
                );

        assertThat(exception.getMessage())
                .isEqualTo("Article not found");
    }

    @Test
    void shouldReturnUsersWhoLikedArticle() {

        when(articleRepository.existsById(articleId))
                .thenReturn(true);

        ArticleLike like = new ArticleLike(
                UUID.randomUUID(),
                articleId,
                userId
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<ArticleLike> likesPage =
                new PageImpl<>(List.of(like));

        when(repo.findByArticleIdOrderByIdDesc(
                articleId,
                pageable
        )).thenReturn(likesPage);

        User user = new User();

        user.setId(userId);
        user.setUsername("sayan");

        when(userRepository.findAllById(any()))
                .thenReturn(List.of(user));

        Page<UserLikeResponse> response =
                likeService.getUsersWhoLiked(
                        articleId,
                        pageable
                );

        assertThat(response).isNotNull();

        assertThat(response.getContent())
                .hasSize(1);

        UserLikeResponse dto =
                response.getContent().get(0);

        assertThat(dto.getId())
                .isEqualTo(userId);

        assertThat(dto.getUsername())
                .isEqualTo("sayan");
    }

    @Test
    void shouldReturnEmptyPageWhenNoLikesExist() {

        when(articleRepository.existsById(articleId))
                .thenReturn(true);

        Pageable pageable = PageRequest.of(0, 10);

        when(repo.findByArticleIdOrderByIdDesc(
                articleId,
                pageable
        )).thenReturn(Page.empty(pageable));

        Page<UserLikeResponse> response =
                likeService.getUsersWhoLiked(
                        articleId,
                        pageable
                );

        assertThat(response).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenArticleNotFoundForLikedUsers() {

        when(articleRepository.existsById(articleId))
                .thenReturn(false);

        Pageable pageable = PageRequest.of(0, 10);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> likeService.getUsersWhoLiked(
                                articleId,
                                pageable
                        )
                );

        assertThat(exception.getMessage())
                .isEqualTo("Article not found");
    }
}