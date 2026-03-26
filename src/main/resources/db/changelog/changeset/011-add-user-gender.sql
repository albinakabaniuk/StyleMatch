-- Liquibase ChangeSet: 011-add-user-gender
-- Description: Add gender column to users table for personalized AI styling

ALTER TABLE users ADD COLUMN gender VARCHAR(50);
