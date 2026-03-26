-- liquibase formatted sql

-- changeset albinakabaniuk:3
CREATE TABLE analysis_results (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    input_type VARCHAR(50) NOT NULL,
    color_type VARCHAR(50),
    undertone VARCHAR(50),
    contrast_level VARCHAR(50),
    raw_data TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_analysis_results_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE analysis_answers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    analysis_id UUID NOT NULL,
    question_key VARCHAR(255) NOT NULL,
    answer_value VARCHAR(255) NOT NULL,
    CONSTRAINT fk_analysis_answers_result FOREIGN KEY (analysis_id) REFERENCES analysis_results (id) ON DELETE CASCADE
);
