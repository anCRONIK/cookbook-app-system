--liquibase formatted sql
--changeset Nikola Presecki:db-init

-- Create a keyspace
CREATE SCHEMA IF NOT EXISTS cookbook;

CREATE SEQUENCE cookbook.user_seq
    START WITH 1
    INCREMENT BY 1 ;

-- Create users table
CREATE TABLE cookbook.users (
    id BIGINT DEFAULT nextval('cookbook.user_seq') PRIMARY KEY,
    username VARCHAR(12) NOT NULL,
    email VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR NOT NULL UNIQUE,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_locked BOOLEAN DEFAULT FALSE,
    is_disabled BOOLEAN DEFAULT FALSE,
    is_editor BOOLEAN DEFAULT FALSE,
    require_password_reset BOOLEAN DEFAULT FALSE
);

-- Create admins table
CREATE TABLE cookbook.admins (
    id BIGINT DEFAULT nextval('cookbook.user_seq') PRIMARY KEY,
    username VARCHAR(12) NOT NULL UNIQUE,
    email VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_locked BOOLEAN DEFAULT FALSE,
    is_disabled BOOLEAN DEFAULT FALSE,
    require_password_reset BOOLEAN DEFAULT FALSE
);

-- Create login_attempts table
CREATE TABLE cookbook.login_attempts (
    id BIGINT GENERATED ALWAYS AS IDENTITY(MINVALUE 1 START WITH 1 CACHE 20) PRIMARY KEY,
    user_id BIGINT UNIQUE,
    admin_id BIGINT UNIQUE,
    attempt_counter INT NOT NULL CHECK(attempt_counter BETWEEN 0 AND 10),
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES cookbook.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_admin FOREIGN KEY(admin_id) REFERENCES cookbook.admins(id) ON DELETE CASCADE
);