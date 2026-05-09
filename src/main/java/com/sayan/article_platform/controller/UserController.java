package com.sayan.article_platform.controller;

import com.sayan.article_platform.dto.response.UserMentionResponse;
import com.sayan.article_platform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserMentionResponse>> searchUsers(
            @RequestParam String query
    ) {
        return ResponseEntity.ok(
                service.searchUsers(query)
        );
    }
}
