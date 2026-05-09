package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.request.CreateCommentRequest;
import com.sayan.article_platform.dto.response.AuthorDto;
import com.sayan.article_platform.dto.response.CommentTreeResponse;
import com.sayan.article_platform.entity.Comment;
import com.sayan.article_platform.entity.Mention;
import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.exception.ResourceNotFoundException;
import com.sayan.article_platform.repository.ArticleRepository;
import com.sayan.article_platform.repository.CommentRepository;
import com.sayan.article_platform.repository.MentionRepository;
import com.sayan.article_platform.repository.UserRepository;
import com.sayan.article_platform.util.MentionParser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;
    private final MentionRepository mentionRepo;
    private final UserRepository userRepo;
    private final MentionParser mentionParser;
    private final ArticleRepository articleRepository;

    public CommentServiceImpl(CommentRepository commentRepo,
                              MentionRepository mentionRepo,
                              UserRepository userRepo,
                              MentionParser mentionParser,
                              ArticleRepository articleRepository) {
        this.commentRepo = commentRepo;
        this.mentionRepo = mentionRepo;
        this.userRepo = userRepo;
        this.mentionParser = mentionParser;
        this.articleRepository = articleRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "comments", key = "#request.articleId()")
    public UUID reply(CreateCommentRequest request, UUID userId) {

        Comment c = new Comment();
        c.setId(UUID.randomUUID());
        c.setArticleId(request.articleId());
        c.setUserId(userId);
        c.setParentCommentId(request.parentCommentId());
        c.setContent(request.content());
        c.setCreatedAt(LocalDateTime.now());

        commentRepo.save(c);

        mentionParser.parse(request.content()).forEach(username ->
                userRepo.findByUsername(username).ifPresent(user -> {
                    mentionRepo.save(new Mention(
                            UUID.randomUUID(),
                            user.getId(),
                            "COMMENT",
                            c.getId(),
                            LocalDateTime.now()
                    ));
                })
        );

        return c.getId();
    }

    @Override
    public List<CommentTreeResponse> getCommentsTree(UUID articleId) {

        // 1. Validate article
        if (!articleRepository.existsById(articleId)) {
            throw new ResourceNotFoundException("Article not found");
        }

        // 2. Fetch all comments
        List<Comment> comments =
                commentRepo.findByArticleIdOrderByCreatedAtDesc(articleId);

        if (comments.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. Fetch all users in ONE query
        Set<UUID> userIds = comments.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());

        Map<UUID, User> userMap = userRepo.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 4. Map comments → DTO
        Map<UUID, CommentTreeResponse> map = new HashMap<>();

        for (Comment c : comments) {

            User user = userMap.get(c.getUserId());

            CommentTreeResponse dto = CommentTreeResponse.builder()
                    .id(c.getId())
                    .content(c.getContent())
                    .createdAt(c.getCreatedAt())
                    .author(
                            AuthorDto.builder()
                                    .id(user.getId())
                                    .username(user.getUsername())
                                    .build()
                    )
                    .replies(new ArrayList<>())
                    .build();

            map.put(c.getId(), dto);
        }

        // 5. Build tree
        List<CommentTreeResponse> roots = new ArrayList<>();

        for (Comment c : comments) {

            if (c.getParentCommentId() == null) {

                roots.add(map.get(c.getId()));

            } else {

                CommentTreeResponse parent =
                        map.get(c.getParentCommentId());

                if (parent != null) {

                    parent.getReplies()
                            .add(map.get(c.getId()));
                }
            }
        }

        // 6. Sort replies recursively (latest first)
        sortRepliesByLatest(roots);

        return roots;
    }

    private void sortRepliesByLatest(
            List<CommentTreeResponse> comments
    ) {

        comments.sort(
                Comparator.comparing(
                        CommentTreeResponse::getCreatedAt
                ).reversed()
        );

        for (CommentTreeResponse comment : comments) {

            if (comment.getReplies() != null
                    && !comment.getReplies().isEmpty()) {

                sortRepliesByLatest(comment.getReplies());
            }
        }
    }
}