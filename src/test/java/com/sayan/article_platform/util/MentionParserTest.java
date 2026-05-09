package com.sayan.article_platform.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MentionParserTest {

    private MentionParser mentionParser;

    @BeforeEach
    void setUp() {

        mentionParser = new MentionParser();
    }

    @Test
    void shouldExtractSingleMention() {

        String text = "Hello @sayan";

        List<String> result =
                mentionParser.parse(text);

        assertThat(result)
                .hasSize(1)
                .contains("sayan");
    }

    @Test
    void shouldExtractMultipleMentions() {

        String text =
                "Hello @sayan and @john welcome";

        List<String> result =
                mentionParser.parse(text);

        assertThat(result)
                .hasSize(2)
                .contains("sayan", "john");
    }

    @Test
    void shouldReturnEmptyListWhenNoMentionsExist() {

        String text =
                "This text contains no mentions";

        List<String> result =
                mentionParser.parse(text);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldIgnoreInvalidMentions() {

        String text =
                "Invalid mentions @ @@@ @#test";

        List<String> result =
                mentionParser.parse(text);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldExtractMentionWithNumbersAndUnderscores() {

        String text =
                "Hello @sayan_123 welcome";

        List<String> result =
                mentionParser.parse(text);

        assertThat(result)
                .containsExactly("sayan_123");
    }

    @Test
    void shouldExtractDuplicateMentions() {

        String text =
                "@sayan hello again @sayan";

        List<String> result =
                mentionParser.parse(text);

        assertThat(result)
                .hasSize(2)
                .containsExactly("sayan", "sayan");
    }

    @Test
    void shouldHandleEmptyString() {

        String text = "";

        List<String> result =
                mentionParser.parse(text);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldHandleNullInput() {

        List<String> result =
                mentionParser.parse(null);

        assertThat(result).isEmpty();
    }
}