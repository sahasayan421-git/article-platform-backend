package com.sayan.article_platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayan.article_platform.dto.response.ArticleResponse;
import com.sayan.article_platform.security.JwtUtil;
import com.sayan.article_platform.security.SecurityConfig;
import com.sayan.article_platform.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ArticleController.class)
@Import({JwtUtil.class, SecurityConfig.class})
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldReturnCurrentUsersDraftArticles() throws Exception {

        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generate(userId);

        ArticleResponse articleResponse = ArticleResponse.builder()
                .id(UUID.randomUUID())
                .title("Draft Article")
                .content("Draft Content")
                .published(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Page<ArticleResponse> responsePage =
                new PageImpl<>(
                        List.of(articleResponse),
                        PageRequest.of(0, 10),
                        1
                );

        when(articleService.getMyDrafts(PageRequest.of(0, 10)))
                .thenReturn(responsePage);

        mockMvc.perform(
                        get("/articles/drafts")
                                .param("page", "0")
                                .param("size", "10")
                                .header("Authorization", "Bearer " + token)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content").isArray())

                .andExpect(jsonPath("$.content[0].title")
                        .value("Draft Article"))

                .andExpect(jsonPath("$.content[0].content")
                        .value("Draft Content"))

                .andExpect(jsonPath("$.content[0].published")
                        .value(false))

                .andExpect(jsonPath("$.totalElements")
                        .value(1));
    }

    @Test
    void shouldReturnEmptyDraftList() throws Exception {

        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generate(userId);

        Page<ArticleResponse> emptyPage =
                Page.empty(PageRequest.of(0, 10));

        when(articleService.getMyDrafts(PageRequest.of(0, 10)))
                .thenReturn(emptyPage);

        mockMvc.perform(
                        get("/articles/drafts")
                                .param("page", "0")
                                .param("size", "10")
                                .header("Authorization", "Bearer " + token)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content").isEmpty())

                .andExpect(jsonPath("$.totalElements")
                        .value(0));
    }
}