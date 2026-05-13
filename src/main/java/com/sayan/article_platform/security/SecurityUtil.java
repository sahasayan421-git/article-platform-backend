package com.sayan.article_platform.security;

import com.sayan.article_platform.model.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtil {

    public static UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("Unauthorized");
        }

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        return principal.getId();
    }
}