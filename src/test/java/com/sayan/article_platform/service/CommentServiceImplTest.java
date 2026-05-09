package com.sayan.article_platform.service;

import com.sayan.article_platform.dto.request.CreateCommentRequest;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepo;

    @Mock
    private MentionRepository mentionRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private MentionParser mentionParser;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private UUID articleId;

    private UUID userId;

    @BeforeEach
    void setUp() {

        articleId = UUID.randomUUID();

        userId = UUID.randomUUID();
    }

    @Test
    void shouldCreateReplySuccessfully() {

        CreateCommentRequest request =
                new CreateCommentRequest(
                        articleId,
                        null,
                        "Nice article @sayan"
                );

        when(mentionParser.parse(request.content()))
                .thenReturn(List.of("sayan"));

        User mentionedUser = new User();

        mentionedUser.setId(UUID.randomUUID());
        mentionedUser.setUsername("sayan");

        when(userRepo.findByUsername("sayan"))
                .thenReturn(Optional.of(mentionedUser));

        UUID commentId =
                commentService.reply(request, userId);

        assertThat(commentId).isNotNull();

        ArgumentCaptor<Comment> commentCaptor =
                ArgumentCaptor.forClass(Comment.class);

        verify(commentRepo).save(commentCaptor.capture());

        Comment savedComment = commentCaptor.getValue();

        assertThat(savedComment.getArticleId())
                .isEqualTo(articleId);

        assertThat(savedComment.getUserId())
                .isEqualTo(userId);

        assertThat(savedComment.getContent())
                .isEqualTo("Nice article @sayan");

        verify(mentionRepo).save(any(Mention.class));
    }

    @Test
    void shouldCreateReplyWithoutMentions() {

        CreateCommentRequest request =
                new CreateCommentRequest(
                        articleId,
                        null,
                        "Simple comment"
                );

        when(mentionParser.parse(request.content()))
                .thenReturn(List.of());

        UUID commentId =
                commentService.reply(request, userId);

        assertThat(commentId).isNotNull();

        verify(commentRepo).save(any(Comment.class));
    }

    @Test
    void shouldReturnCommentTree() {

        UUID rootCommentId = UUID.randomUUID();

        UUID replyCommentId = UUID.randomUUID();

        Comment root = new Comment();

        root.setId(rootCommentId);
        root.setArticleId(articleId);
        root.setUserId(userId);
        root.setContent("Root Comment");
        root.setParentCommentId(null);
        root.setCreatedAt(LocalDateTime.now());

        Comment reply = new Comment();

        reply.setId(replyCommentId);
        reply.setArticleId(articleId);
        reply.setUserId(userId);
        reply.setContent("Reply Comment");
        reply.setParentCommentId(rootCommentId);
        reply.setCreatedAt(LocalDateTime.now());

        when(articleRepository.existsById(articleId))
                .thenReturn(true);

        when(commentRepo
                .findByArticleIdOrderByCreatedAtDesc(articleId))
                .thenReturn(List.of(reply, root));

        User user = new User();

        user.setId(userId);
        user.setUsername("sayan");

        when(userRepo.findAllById(any()))
                .thenReturn(List.of(user));

        List<CommentTreeResponse> result =
                commentService.getCommentsTree(articleId);

        assertThat(result).hasSize(1);

        CommentTreeResponse rootResponse = result.get(0);

        assertThat(rootResponse.getContent())
                .isEqualTo("Root Comment");

        assertThat(rootResponse.getReplies())
                .hasSize(1);

        assertThat(rootResponse.getReplies().get(0).getContent())
                .isEqualTo("Reply Comment");
    }

    @Test
    void shouldThrowExceptionWhenArticleNotFound() {

        when(articleRepository.existsById(articleId))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> commentService.getCommentsTree(articleId)
                );

        assertThat(exception.getMessage())
                .isEqualTo("Article not found");
    }

    @Test
    void shouldReturnEmptyListWhenNoCommentsExist() {

        when(articleRepository.existsById(articleId))
                .thenReturn(true);

        when(commentRepo
                .findByArticleIdOrderByCreatedAtDesc(articleId))
                .thenReturn(List.of());

        List<CommentTreeResponse> result =
                commentService.getCommentsTree(articleId);

        assertThat(result).isEmpty();
    }
}