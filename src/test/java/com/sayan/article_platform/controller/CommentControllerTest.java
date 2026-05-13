package com.sayan.article_platform.controller;

import com.sayan.article_platform.dto.request.CreateCommentRequest;
import com.sayan.article_platform.security.JwtUtil;
import com.sayan.article_platform.security.SecurityConfig;
import com.sayan.article_platform.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void reply_success() throws Exception {

        UUID responseId = UUID.randomUUID();

        CreateCommentRequest req =
                new CreateCommentRequest(UUID.randomUUID(), null, "hello");

        Mockito.when(service.reply(Mockito.any(), Mockito.any()))
                .thenReturn(responseId);

        mockMvc.perform(post("/api/comments")
                        //.header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void reply_validation_fail() throws Exception {

        CreateCommentRequest req =
                new CreateCommentRequest(null, null, "");

        mockMvc.perform(post("/api/comments")
                        //.header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}