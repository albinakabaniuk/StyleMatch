-- liquibase formatted sql

-- changeset auto:2
-- validCheckSum: 9:caf8b52514ef4f6de87f21c7998b0bef
CREATE TABLE users (
    id UUID DEFAULT ${uuid_function} PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);
