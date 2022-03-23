--liquibase formatted sql
--changeset Nikola Presecki:db-init

-- Create a keyspace
CREATE SCHEMA IF NOT EXISTS cookbook;

CREATE SEQUENCE cookbook.user_seq
    START WITH 1
    INCREMENT BY 1 ;

-- Create users table
CREATE TABLE cookbook.users (
    id BIGINT DEFAULT nextval('user_seq') PRIMARY KEY,
    username VARCHAR(12) NOT NULL,
    email VARCHAR(30) NOT NULL,
    password_hash VARCHAR NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_locked BOOLEAN,
    is_disabled BOOLEAN,
    is_editor BOOLEAN,
    require_password_reset BOOLEAN,
    CONSTRAINT unq_usr UNIQUE (username,email)
);

-- Create admins table
CREATE TABLE cookbook.admins (
    id BIGINT DEFAULT nextval('user_seq') PRIMARY KEY,
    username VARCHAR(12) NOT NULL,
    email VARCHAR(30) NOT NULL,
    password_hash VARCHAR NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_locked BOOLEAN,
    is_disabled BOOLEAN,
    require_password_reset BOOLEAN,
    CONSTRAINT unq_adm UNIQUE (username,email)
);

-- Create login_attempts table
CREATE TABLE cookbook.login_attempts (
    id BIGINT GENERATED ALWAYS AS IDENTITY(MINVALUE 1 START WITH 1 CACHE 20) PRIMARY KEY,
    user_id BIGINT,
    admin_id BIGINT,
    attempt_counter INT NOT NULL CHECK(attempt_counter BETWEEN 0 AND 10),
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_admin FOREIGN KEY(admin_id) REFERENCES admins(id) ON DELETE CASCADE
);