-- ============================================
-- V1: Enable required PostgreSQL extensions
-- ============================================
-- uuid-ossp → UUID generation
-- pg_trgm   → optional (for future search optimizations)

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";