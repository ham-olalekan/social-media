CREATE TABLE user_followers (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    follower_user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_user_followers PRIMARY KEY (id),
    INDEX index_user_id (user_id),
    INDEX index_follower_user_id (follower_user_id),
    CONSTRAINT FK_USER_FOLLOWED FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT FK_USER_FOLLOWER FOREIGN KEY (follower_user_id) REFERENCES users(id)
);