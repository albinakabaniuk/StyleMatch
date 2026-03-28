-- liquibase formatted sql

-- changeset albinakabaniuk:1
-- validCheckSum: 9:0700d3e1b44d10a5b59f564362156903
CREATE TABLE customers (
    id UUID DEFAULT ${uuid_function} PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    color_type VARCHAR(50) NOT NULL,
    body_shape VARCHAR(50) NOT NULL,
    contrast_level VARCHAR(50) NOT NULL,
    undertone VARCHAR(50),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- rollback DROP TABLE customers;
