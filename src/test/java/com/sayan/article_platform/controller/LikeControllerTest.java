package com.sayan.article_platform.controller;

import com.sayan.article_platform.dto.response.LikesCountResponse;
import com.sayan.article_platform.dto.response.UserLikeResponse;
import com.sayan.article_platform.model.UserPrincipal;
import com.sayan.article_platform.entity.User;
import com.sayan.article_platform.service.LikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    private UserPrincipal createMockPrincipal(UUID userId) {
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testuser");
        return new UserPrincipal(mockUser);
    }

    @Test
    void shouldLikeArticleSuccessfully() throws Exception {

        UUID articleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        doNothing().when(likeService)
                .likeArticle(articleId, userId);

        UserPrincipal principal = createMockPrincipal(userId);

        mockMvc.perform(
                        post("/api/likes/article/{id}", articleId)
                                .with(user(principal))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnLikesCountSuccessfully() throws Exception {

        UUID articleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        LikesCountResponse response =
                new LikesCountResponse(articleId, 5L);

        when(likeService.getLikesCount(articleId))
                .thenReturn(response);

        UserPrincipal principal = createMockPrincipal(userId);

        mockMvc.perform(
                        get("/api/likes/{articleId}/likes/count", articleId)
                                .with(user(principal))
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.articleId")
                        .value(articleId.toString()))

                .andExpect(jsonPath("$.likesCount")
                        .value(5));
    }

    @Test
    void shouldReturnUsersWhoLikedArticle() throws Exception {

        UUID articleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        UserPrincipal principal = createMockPrincipal(userId);

        UserLikeResponse user1 =
                UserLikeResponse.builder()
                        .id(UUID.randomUUID())
                        .username("sayan")
                        .build();

        UserLikeResponse user2 =
                UserLikeResponse.builder()
                        .id(UUID.randomUUID())
                        .username("john")
                        .build();

        when(likeService.getUsersWhoLiked(
                eq(articleId),
                any()
        )).thenReturn(
                new PageImpl<>(
                        List.of(user1, user2),
                        PageRequest.of(0, 10),
                        2
                )
        );

        mockMvc.perform(
                        get("/api/likes/{articleId}/likes", articleId)
                                .with(user(principal))
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content[0].username")
                        .value("sayan"))

                .andExpect(jsonPath("$.content[1].username")
                        .value("john"))

                .andExpect(jsonPath("$.content.length()")
                        .value(2));
    }
}