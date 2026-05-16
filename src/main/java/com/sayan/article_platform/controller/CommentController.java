package com.sayan.article_platform.controller;

import com.sayan.article_platform.dto.request.CreateCommentRequest;
import com.sayan.article_platform.dto.response.CommentTreeResponse;
import com.sayan.article_platform.model.UserPrincipal;
import com.sayan.article_platform.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> reply(
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(service.reply(request, principal.getId()));
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<CommentTreeResponse>> getComments(
            @PathVariable UUID articleId) {

        return ResponseEntity.ok(
                service.getCommentsTree(articleId)
        );
    }
}