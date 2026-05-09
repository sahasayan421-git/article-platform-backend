package com.sayan.article_platform.repository;

import com.sayan.article_platform.entity.Article;
import com.sayan.article_platform.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return current user's draft articles")
    void shouldReturnCurrentUsersDraftArticles() {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("Sayan");
        user.setEmail("sayan@test.com");
        user.setPasswordHash("password");

        userRepository.save(user);

        Article draftArticle = new Article();
        draftArticle.setId(UUID.randomUUID());
        draftArticle.setAuthorId(user.getId());
        draftArticle.setTitle("Draft Article");
        draftArticle.setContent("Draft Content");
        draftArticle.setIsPublished(false);
        draftArticle.setCreatedAt(LocalDateTime.now());

        articleRepository.save(draftArticle);

        Article publishedArticle = new Article();
        publishedArticle.setId(UUID.randomUUID());
        publishedArticle.setAuthorId(user.getId());
        publishedArticle.setTitle("Published Article");
        publishedArticle.setContent("Published Content");
        publishedArticle.setIsPublished(true);
        publishedArticle.setCreatedAt(LocalDateTime.now());

        articleRepository.save(publishedArticle);

        Page<Article> result =
                articleRepository
                        .findByAuthorIdAndIsPublishedFalseOrderByCreatedAtDesc(
                                user.getId(),
                                PageRequest.of(0, 10)
                        );

        assertThat(result).isNotNull();

        assertThat(result.getContent()).hasSize(1);

        assertThat(result.getContent().get(0).getTitle())
                .isEqualTo("Draft Article");

        assertThat(result.getContent().get(0).getIsPublished())
                .isFalse();
    }

    @Test
    @DisplayName("Should return empty page when no drafts exist")
    void shouldReturnEmptyPageWhenNoDraftsExist() {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("No Draft User");
        user.setEmail("nodraft@test.com");
        user.setPasswordHash("password");

        userRepository.save(user);

        Page<Article> result =
                articleRepository
                        .findByAuthorIdAndIsPublishedFalseOrderByCreatedAtDesc(
                                user.getId(),
                                PageRequest.of(0, 10)
                        );

        assertThat(result).isNotNull();

        assertThat(result.getContent()).isEmpty();
    }
}