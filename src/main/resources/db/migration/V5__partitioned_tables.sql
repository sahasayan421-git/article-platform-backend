-- ============================================
-- V4: Create partitioned tables (comments, article_likes)
-- ============================================

-- --------------------------------------------
-- COMMENTS (RANGE partitioned)
-- --------------------------------------------

-- Rename old table (if exists)
ALTER TABLE IF EXISTS comments RENAME TO comments_old;

CREATE TABLE comments (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    article_id UUID NOT NULL,
    user_id UUID NOT NULL,
    parent_comment_id UUID,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT pk_comments PRIMARY KEY (id, created_at),

    CONSTRAINT fk_comments_article
        FOREIGN KEY (article_id)
        REFERENCES articles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_no_self_parent
        CHECK (parent_comment_id IS NULL OR parent_comment_id <> id)
)
PARTITION BY RANGE (created_at);


-- --------------------------------------------
-- ARTICLE LIKES (HASH partitioned)
-- --------------------------------------------

-- Rename old table (if exists)
ALTER TABLE IF EXISTS article_likes RENAME TO article_likes_old;

CREATE TABLE article_likes (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    article_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_article_likes PRIMARY KEY (id, article_id),

    CONSTRAINT fk_article_likes_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_article_likes_article
        FOREIGN KEY (article_id)
        REFERENCES articles(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_user_article_like_partitioned UNIQUE (user_id, article_id)
)
PARTITION BY HASH (article_id);