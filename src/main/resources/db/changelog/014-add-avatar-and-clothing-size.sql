-- liquibase formatted sql
-- changeset albinakabaniuk:014
ALTER TABLE users ADD COLUMN avatar TEXT;
ALTER TABLE users ADD COLUMN clothing_size VARCHAR(20);
