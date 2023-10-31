CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT NOT NULL,
    reference VARCHAR(64) NOT NULL UNIQUE,
    content VARCHAR(300),
    user_id BIGINT NOT NULL,
    likes BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_post_id PRIMARY KEY (id),
    INDEX index_post_user_id (user_id),
    INDEX index_post_reference (reference)
);