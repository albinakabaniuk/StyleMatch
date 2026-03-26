-- liquibase formatted sql

-- changeset albinakabaniuk:7
ALTER TABLE users
    ADD COLUMN name VARCHAR(255),
    ADD COLUMN age INTEGER,
    ADD COLUMN weight DOUBLE PRECISION,
    ADD COLUMN height DOUBLE PRECISION;

-- rollback ALTER TABLE users DROP COLUMN name, DROP COLUMN age, DROP COLUMN weight, DROP COLUMN height;
