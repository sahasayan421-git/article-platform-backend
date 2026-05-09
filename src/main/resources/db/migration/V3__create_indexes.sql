-- ============================================
-- V3: Create indexes for performance
-- ============================================

-- USERS
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- ARTICLES
CREATE INDEX IF NOT EXISTS idx_articles_author_time
    ON articles (author_id, created_at DESC);

-- Feed optimization (published articles)
CREATE INDEX IF NOT EXISTS idx_articles_feed
    ON articles (is_published, created_at DESC);

-- Cursor-based pagination
CREATE INDEX IF NOT EXISTS idx_articles_cursor
    ON articles (created_at DESC, id);

-- Partial index for published articles
CREATE INDEX IF NOT EXISTS idx_articles_published_partial
    ON articles (created_at DESC)
    WHERE is_published = TRUE;

-- Covering index (reduces heap access)
CREATE INDEX IF NOT EXISTS idx_articles_covering
    ON articles (is_published, created_at DESC)
    INCLUDE (title, author_id);

-- Full-text search
CREATE INDEX IF NOT EXISTS idx_articles_search
    ON articles
    USING GIN (to_tsvector('english', title || ' ' || content));

-- COMMENTS
CREATE INDEX IF NOT EXISTS idx_comments_article_time
    ON comments (article_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_comments_parent_time
    ON comments (parent_comment_id, created_at);

-- For tree traversal
CREATE INDEX IF NOT EXISTS idx_comments_article_parent
    ON comments (article_id, parent_comment_id);

-- Top-level comments (very important)
CREATE INDEX IF NOT EXISTS idx_comments_top_level
    ON comments (article_id, created_at DESC)
    WHERE parent_comment_id IS NULL;

-- ARTICLE LIKES
CREATE INDEX IF NOT EXISTS idx_article_likes_article
    ON article_likes (article_id);

CREATE INDEX IF NOT EXISTS idx_article_likes_user
    ON article_likes (user_id);

CREATE INDEX IF NOT EXISTS idx_article_likes_article_created
    ON article_likes (article_id, created_at);

-- COMMENT LIKES
CREATE INDEX IF NOT EXISTS idx_comment_likes_comment
    ON comment_likes (comment_id);

-- MENTIONS
CREATE INDEX IF NOT EXISTS idx_mentions_user
    ON mentions (mentioned_user_id);

CREATE INDEX IF NOT EXISTS idx_mentions_source
    ON mentions (source_type, source_id);