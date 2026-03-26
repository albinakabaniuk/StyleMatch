-- liquibase formatted sql
-- changeset albinakabaniuk:5
ALTER TABLE analysis_answers RENAME COLUMN question_key TO question_id;
ALTER TABLE analysis_answers ALTER COLUMN question_id TYPE INTEGER USING question_id::integer;
