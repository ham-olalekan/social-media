CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(64) NOT NULL UNIQUE,
    first_name VARCHAR(20) NOT NULL,
    middle_name VARCHAR(20) NULL,
    last_name VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(512) NOT NULL,
    country_code VARCHAR(5) NULL,
    phone_no VARCHAR(20) NOT NULL UNIQUE,
    image_url VARCHAR(255) NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    INDEX index_email (email),
    INDEX index_username (username),
    INDEX index_phone_no (phone_no)
);