
DROP TYPE IF EXISTS lang_code CASCADE;;
CREATE TYPE lang_code AS ENUM ('en','ja','zh-CN','ko');;

DROP TYPE IF EXISTS post_block_type CASCADE;;
CREATE TYPE post_block_type AS ENUM ('paragraph','image');;

CREATE TABLE IF NOT EXISTS universities (
  code   TEXT PRIMARY KEY,
  labels JSONB NOT NULL
);;

CREATE TABLE IF NOT EXISTS users (
  id       SERIAL PRIMARY KEY,
  email    VARCHAR(255) UNIQUE NOT NULL,
  name     VARCHAR(100),
  password VARCHAR(255)
);;

CREATE TABLE IF NOT EXISTS tags (
  id                 SERIAL PRIMARY KEY,
  slug               TEXT UNIQUE NOT NULL,
  labels             JSONB NOT NULL,
  "group"            TEXT NOT NULL CHECK ("group" IN ('user','school','notice')),
  is_user_selectable BOOLEAN NOT NULL DEFAULT TRUE        -- user/school: true, notice: false
);;
CREATE INDEX IF NOT EXISTS idx_tags_group ON tags("group");;

CREATE TABLE IF NOT EXISTS posts (
  id            BIGSERIAL PRIMARY KEY,
  author_id     INT REFERENCES users(id) ON DELETE SET NULL,
  lang          lang_code NOT NULL DEFAULT 'en',
  title         TEXT NOT NULL CHECK (char_length(title) BETWEEN 1 AND 60),
  content       TEXT,
  tags          INT[] NOT NULL DEFAULT '{}',
  view_count    BIGINT NOT NULL DEFAULT 0 CHECK (view_count >= 0),
  content_text  TEXT,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at    TIMESTAMPTZ
);;

CREATE INDEX IF NOT EXISTS idx_posts_tags_gin ON posts USING GIN (tags);;
CREATE INDEX IF NOT EXISTS idx_posts_created_at ON posts (created_at DESC);;

CREATE OR REPLACE FUNCTION set_updated_at_posts()
RETURNS trigger AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;;

DROP TRIGGER IF EXISTS trg_posts_updated_at ON posts;;
CREATE TRIGGER trg_posts_updated_at
BEFORE UPDATE ON posts
FOR EACH ROW
EXECUTE FUNCTION set_updated_at_posts();;

CREATE TABLE IF NOT EXISTS post_blocks (
  id         BIGSERIAL PRIMARY KEY,
  post_id    BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
  idx        INT    NOT NULL,
  type       post_block_type NOT NULL,
  text       TEXT,
  image_url  TEXT,
  CONSTRAINT uq_blocks_post_idx UNIQUE (post_id, idx),
  CONSTRAINT chk_block_fields CHECK (
    (type = 'paragraph' AND text IS NOT NULL AND image_url IS NULL)
    OR
    (type = 'image' AND image_url IS NOT NULL AND text IS NULL)
  ),
  CONSTRAINT chk_image_https CHECK (
    type <> 'image' OR image_url ~ '^https?://'
  )
);;
CREATE INDEX IF NOT EXISTS idx_blocks_post ON post_blocks (post_id, idx);;


CREATE OR REPLACE FUNCTION refresh_post_derived_and_limits(p_id BIGINT)
RETURNS VOID AS $$
DECLARE
  merged_text TEXT;
  block_cnt   INT;
  image_cnt   INT;
BEGIN
  SELECT count(*), count(*) FILTER (WHERE type = 'image')
  INTO block_cnt, image_cnt
  FROM post_blocks
  WHERE post_id = p_id;

  IF block_cnt > 20 THEN
    RAISE EXCEPTION 'Too many blocks (%). Max is 20.', block_cnt
      USING ERRCODE = '23514';
  END IF;

  IF image_cnt > 6 THEN
    RAISE EXCEPTION 'Too many image blocks (%). Max is 6.', image_cnt
      USING ERRCODE = '23514';
  END IF;

  SELECT string_agg(pb.text, E'\n' ORDER BY pb.idx)
  INTO merged_text
  FROM post_blocks pb
  WHERE pb.post_id = p_id AND pb.type = 'paragraph';

  UPDATE posts
  SET content_text = COALESCE(merged_text, '')
  WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION trg_blocks_after_change()
RETURNS trigger AS $$
BEGIN
  IF (TG_OP = 'DELETE') THEN
    PERFORM refresh_post_derived_and_limits(OLD.post_id);
  ELSE
    PERFORM refresh_post_derived_and_limits(NEW.post_id);
  END IF;
  RETURN NULL;
END;
$$ LANGUAGE plpgsql;;

DROP TRIGGER IF EXISTS trg_blocks_aiud ON post_blocks;;
CREATE TRIGGER trg_blocks_aiud
AFTER INSERT OR UPDATE OR DELETE ON post_blocks
FOR EACH ROW
EXECUTE FUNCTION trg_blocks_after_change();;


CREATE OR REPLACE FUNCTION incr_post_view(p_id BIGINT)
RETURNS VOID AS $$
BEGIN
  UPDATE posts
  SET view_count = view_count + 1
  WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;;