-- liquibase formatted sql

-- changeset albinakabaniuk:9
UPDATE customers SET body_shape = 'HOURGLASS' WHERE body_shape = 'X';
UPDATE customers SET body_shape = 'RECTANGLE' WHERE body_shape = 'H';
UPDATE customers SET body_shape = 'TRIANGLE' WHERE body_shape = 'A';
UPDATE customers SET body_shape = 'INVERTED_TRIANGLE' WHERE body_shape = 'V';
UPDATE customers SET body_shape = 'OVAL' WHERE body_shape = 'O';

-- rollback UPDATE customers SET body_shape = 'X' WHERE body_shape = 'HOURGLASS';
-- rollback UPDATE customers SET body_shape = 'H' WHERE body_shape = 'RECTANGLE';
-- rollback UPDATE customers SET body_shape = 'A' WHERE body_shape = 'TRIANGLE';
-- rollback UPDATE customers SET body_shape = 'V' WHERE body_shape = 'INVERTED_TRIANGLE';
-- rollback UPDATE customers SET body_shape = 'O' WHERE body_shape = 'OVAL';
