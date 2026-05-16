package com.sayan.article_platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayan.article_platform.dto.request.CreateCommentRequest;
import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.model.UserPrincipal;
import com.sayan.article_platform.service.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService service;

    @Autowired
    private ObjectMapper mapper;

    private UserPrincipal createMockPrincipal(UUID userId) {

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testuser");

        return new UserPrincipal(mockUser);
    }

    @Test
    void reply_success() throws Exception {

        UUID userId = UUID.randomUUID();
        UUID responseId = UUID.randomUUID();

        CreateCommentRequest req =
                new CreateCommentRequest(
                        UUID.randomUUID(),
                        null,
                        "hello"
                );

        Mockito.when(service.reply(Mockito.any(), Mockito.any()))
                .thenReturn(responseId);

        UserPrincipal principal = createMockPrincipal(userId);

        mockMvc.perform(
                        post("/api/comments")
                                .with(user(principal))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(req))
                )
                .andExpect(status().isOk());
    }

    @Test
    void reply_validation_fail() throws Exception {

        UUID userId = UUID.randomUUID();

        UserPrincipal principal = createMockPrincipal(userId);

        CreateCommentRequest req =
                new CreateCommentRequest(
                        null,
                        null,
                        ""
                );

        mockMvc.perform(
                        post("/api/comments")
                                .with(user(principal))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(req))
                )
                .andExpect(status().isBadRequest());
    }
}