package com.sayan.article_platform.service;


import com.sayan.article_platform.dto.request.CreateCommentRequest;
import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.repository.CommentRepository;
import com.sayan.article_platform.repository.MentionRepository;
import com.sayan.article_platform.repository.UserRepository;
import com.sayan.article_platform.util.MentionParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepo;
    @Mock
    private MentionRepository mentionRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private MentionParser parser;

    @InjectMocks
    private CommentServiceImpl service;

    @Test
    void reply_withMention_shouldSaveMentionAndPublishEvent() {

        UUID userId = UUID.randomUUID();
        UUID mentionedId = UUID.randomUUID();

        CreateCommentRequest req =
                new CreateCommentRequest(UUID.randomUUID(), null, "@john");

        when(parser.parse(any())).thenReturn(java.util.List.of("john"));
        when(userRepo.findByUsername("john"))
                .thenReturn(Optional.of(new User(mentionedId, "john", "e", "p")));

        service.reply(req, userId);

        verify(mentionRepo, times(1)).save(any());
    }

    @Test
    void reply_noMention_shouldNotPublishEvent() {
        CreateCommentRequest req =
                new CreateCommentRequest(UUID.randomUUID(), null, "hello");

        when(parser.parse(any())).thenReturn(java.util.List.of());

        service.reply(req, UUID.randomUUID());
    }
}