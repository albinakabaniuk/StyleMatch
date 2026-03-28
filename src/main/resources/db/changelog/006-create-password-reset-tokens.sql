-- liquibase formatted sql
-- changeset albinakabaniuk:6
-- validCheckSum: 9:1e17b0e199ad4d094a8c94321bb6ab71
CREATE TABLE password_reset_tokens (
    id UUID DEFAULT ${uuid_function} PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_prt_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
