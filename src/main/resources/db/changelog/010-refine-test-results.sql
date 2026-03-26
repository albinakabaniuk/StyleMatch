-- liquibase formatted sql

-- changeset albinakabaniuk:10
-- Add index on user_id for faster lookups in test_results
CREATE INDEX idx_test_results_user_id ON test_results(user_id);

-- Ensure test_type is capped at a reasonable length (already was 50, but consistent)
ALTER TABLE test_results ALTER COLUMN test_type TYPE VARCHAR(100);
