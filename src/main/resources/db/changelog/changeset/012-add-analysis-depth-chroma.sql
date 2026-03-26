-- Liquibase ChangeSet: 012-add-analysis-depth-chroma
-- Description: Add depth and chroma columns to analysis_results table for 12-season styling

ALTER TABLE analysis_results ADD COLUMN depth VARCHAR(50);
ALTER TABLE analysis_results ADD COLUMN chroma VARCHAR(50);
