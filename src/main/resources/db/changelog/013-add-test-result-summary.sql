-- liquibase formatted sql

-- changeset albinakabaniuk:13
-- Add explicit summary column to test_results table to avoid overloading the metadata field
ALTER TABLE test_results ADD COLUMN summary TEXT;
