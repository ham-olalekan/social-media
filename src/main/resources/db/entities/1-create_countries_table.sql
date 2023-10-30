CREATE TABLE countries(
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(100)  NOT NULL,
    iso3 char(3) DEFAULT NULL,
    numeric_code char(3) DEFAULT NULL,
    country_code char(2) DEFAULT NULL,
    phone_code varchar(255) DEFAULT NULL,
    capital varchar(255) DEFAULT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    flag tinyint(1) NOT NULL DEFAULT '1',
    INDEX idx_countries_code (country_code),
    PRIMARY KEY (id)
)