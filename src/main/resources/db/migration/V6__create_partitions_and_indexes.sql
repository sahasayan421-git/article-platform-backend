-- ============================================
-- V5: Create partitions + indexes
-- ============================================

-- --------------------------------------------
-- COMMENTS PARTITIONS (RANGE by month)
-- --------------------------------------------

CREATE TABLE IF NOT EXISTS comments_2026_01
PARTITION OF comments
FOR VALUES FROM ('2026-01-01') TO ('2026-02-01');

CREATE TABLE IF NOT EXISTS comments_2026_02
PARTITION OF comments
FOR VALUES FROM ('2026-02-01') TO ('2026-03-01');

-- Default partition (safety net)
CREATE TABLE IF NOT EXISTS comments_default
PARTITION OF comments DEFAULT;


-- --------------------------------------------
-- ARTICLE_LIKES PARTITIONS (HASH)
-- --------------------------------------------

CREATE TABLE IF NOT EXISTS article_likes_p0
PARTITION OF article_likes
FOR VALUES WITH (MODULUS 4, REMAINDER 0);

CREATE TABLE IF NOT EXISTS article_likes_p1
PARTITION OF article_likes
FOR VALUES WITH (MODULUS 4, REMAINDER 1);

CREATE TABLE IF NOT EXISTS article_likes_p2
PARTITION OF article_likes
FOR VALUES WITH (MODULUS 4, REMAINDER 2);

CREATE TABLE IF NOT EXISTS article_likes_p3
PARTITION OF article_likes
FOR VALUES WITH (MODULUS 4, REMAINDER 3);


-- --------------------------------------------
-- INDEXES (applied on parent → inherited)
-- --------------------------------------------

-- COMMENTS
CREATE INDEX IF NOT EXISTS idx_comments_article_time
ON comments (article_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_comments_parent_time
ON comments (parent_comment_id, created_at);

CREATE INDEX IF NOT EXISTS idx_comments_top_level
ON comments (article_id, created_at DESC)
WHERE parent_comment_id IS NULL;

-- ARTICLE LIKES
CREATE INDEX IF NOT EXISTS idx_article_likes_article
ON article_likes (article_id);

CREATE INDEX IF NOT EXISTS idx_article_likes_user
ON article_likes (user_id);