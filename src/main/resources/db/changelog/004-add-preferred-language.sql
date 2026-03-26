-- liquibase formatted sql
-- changeset albinakabaniuk:4
ALTER TABLE users ADD COLUMN preferred_language VARCHAR(50) DEFAULT 'en';
