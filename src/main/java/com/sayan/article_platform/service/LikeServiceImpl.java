package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.response.LikesCountResponse;
import com.sayan.article_platform.dto.response.UserLikeResponse;
import com.sayan.article_platform.entity.ArticleLike;
import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.exception.ResourceNotFoundException;
import com.sayan.article_platform.repository.ArticleLikeRepository;
import com.sayan.article_platform.repository.ArticleRepository;
import com.sayan.article_platform.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private final ArticleLikeRepository repo;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public LikeServiceImpl(ArticleLikeRepository repo, UserRepository userRepository, ArticleRepository articleRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void likeArticle(UUID articleId, UUID userId) {
        repo.save(new ArticleLike(UUID.randomUUID(), articleId, userId));
    }

    @Override
    public LikesCountResponse getLikesCount(UUID articleId) {

        if (!articleRepository.existsById(articleId)) {
            throw new ResourceNotFoundException("Article not found");
        }

        long count = repo.countByArticleId(articleId);

        return new LikesCountResponse(articleId, count);
    }

    @Override
    public Page<UserLikeResponse> getUsersWhoLiked(
            UUID articleId,
            Pageable pageable) {

        // 1. Validate article
        if (!articleRepository.existsById(articleId)) {
            throw new ResourceNotFoundException("Article not found");
        }

        // 2. Fetch paginated likes
        Page<ArticleLike> likesPage =
                repo.findByArticleIdOrderByIdDesc(articleId, pageable);

        if (likesPage.isEmpty()) {
            return Page.empty(pageable);
        }

        // 3. Extract userIds
        Set<UUID> userIds = likesPage.stream()
                .map(ArticleLike::getUserId)
                .collect(Collectors.toSet());

        // 4. Fetch users in ONE query
        Map<UUID, User> userMap = userRepository.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 5. Map to DTO while preserving pagination
        return likesPage.map(like -> {
            User user = userMap.get(like.getUserId());

            return UserLikeResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .build();
        });
    }
}