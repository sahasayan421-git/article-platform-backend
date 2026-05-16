package com.sayan.article_platform.controller;

import com.sayan.article_platform.dto.response.LikesCountResponse;
import com.sayan.article_platform.dto.response.UserLikeResponse;
import com.sayan.article_platform.model.UserPrincipal;
import com.sayan.article_platform.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService service;

    public LikeController(LikeService service) {
        this.service = service;
    }

    @PostMapping("/article/{id}")
    public ResponseEntity<?> likeArticle(@PathVariable UUID id,
                                         @AuthenticationPrincipal UserPrincipal principal) {
        service.likeArticle(id, principal.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{articleId}/likes/count")
    public ResponseEntity<LikesCountResponse> getLikesCount(
            @PathVariable UUID articleId) {

        return ResponseEntity.ok(
                service.getLikesCount(articleId)
        );
    }

    @GetMapping("/{articleId}/likes")
    public ResponseEntity<Page<UserLikeResponse>> getLikes(
            @PathVariable UUID articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                service.getUsersWhoLiked(articleId, pageable)
        );
    }
}