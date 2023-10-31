CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT NOT NULL,
    reference VARCHAR(64) NOT NULL UNIQUE,
    content VARCHAR(300),
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    upstream_id BIGINT DEFAULT NULL,
    likes BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_comment_id PRIMARY KEY (id),
    INDEX index_comment_user_id (user_id),
    INDEX index_comment_reference (reference),
    INDEX index_comment_post_id (post_id),
    INDEX index_upstream_comment_id(upstream_id)
);