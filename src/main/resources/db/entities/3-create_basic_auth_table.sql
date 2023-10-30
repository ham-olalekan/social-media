CREATE TABLE basic_auths (
    id BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BIT NOT NULL,
    CONSTRAINT pk_basic_auth PRIMARY KEY (id),
    UNIQUE KEY uk_basic_auth_username(username)
);