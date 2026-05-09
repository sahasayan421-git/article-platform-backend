package com.sayan.article_platform.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MentionParser {

    private static final Pattern PATTERN = Pattern.compile("@(\\w+)");

    public List<String> parse(String text) {
        List<String> result = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return result;
        }

        Matcher matcher = PATTERN.matcher(text);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }

        return result;
    }
}