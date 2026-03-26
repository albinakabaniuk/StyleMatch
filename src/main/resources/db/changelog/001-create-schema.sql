-- liquibase formatted sql

-- changeset albinakabaniuk:1
CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
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
