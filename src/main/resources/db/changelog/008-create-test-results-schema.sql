-- liquibase formatted sql

-- changeset albinakabaniuk:8
CREATE TABLE test_results (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    test_type VARCHAR(50) NOT NULL,
    result TEXT NOT NULL,
    metadata TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_test_results_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- rollback DROP TABLE test_results;
