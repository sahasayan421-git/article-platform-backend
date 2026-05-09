package com.sayan.article_platform.controller;

import com.sayan.article_platform.dto.request.CreateArticleRequest;
import com.sayan.article_platform.dto.response.ArticleResponse;
import com.sayan.article_platform.dto.response.CommentTreeResponse;
import com.sayan.article_platform.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    // 🔓 Public
    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getPublished() {
        return ResponseEntity.ok(service.getPublishedArticles());
    }

    // 🔓 Public
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getArticleById(id));
    }

    // 🔒 Auth required
    @PostMapping
    public ResponseEntity<ArticleResponse> create(
            @Valid @RequestBody CreateArticleRequest request) {

        ArticleResponse response = service.createArticle(request);

        return ResponseEntity
                .created(URI.create("/articles/" + response.id()))
                .body(response);
    }

    // 🔒 Owner only
    @PatchMapping("/{id}/publish")
    public ResponseEntity<ArticleResponse> publish(@PathVariable UUID id) {
        return ResponseEntity.ok(service.publishArticle(id));
    }

    // 🔒 Owner only
    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> edit(@PathVariable UUID id, @Valid @RequestBody CreateArticleRequest request) {
        return ResponseEntity.ok(service.editArticle(id, request));
    }

    // 🔒 Owner only
    @GetMapping("/drafts")
    public ResponseEntity<Page<ArticleResponse>> getMyDrafts(
            @PageableDefault(size = 10)
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.getMyDrafts(pageable)
        );
    }

    // 🔓 Owner only
    @GetMapping("/drafts/{id}")
    public ResponseEntity<ArticleResponse> getDraftById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getArticleById(id));
    }
}