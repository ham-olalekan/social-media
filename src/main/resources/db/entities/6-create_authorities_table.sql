CREATE TABLE authorities (
    id BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(20) NOT NULL,
    authority VARCHAR(20) NOT NULL,
    INDEX index_authorities_username (username),
    CONSTRAINT pk_authorities PRIMARY KEY (id),
    UNIQUE KEY uk_username_authority (username, authority)
);