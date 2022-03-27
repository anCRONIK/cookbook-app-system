--liquibase formatted sql
--changeset Nikola Presecki:db-init

-- Create a namespace
CREATE SCHEMA IF NOT EXISTS cookbook;

CREATE SEQUENCE cookbook.user_seq
    START WITH 1
    INCREMENT BY 1;

-- Create users table
CREATE TABLE IF NOT EXISTS cookbook.users (
    id BIGINT DEFAULT nextval('cookbook.user_seq') PRIMARY KEY,
    username VARCHAR(12) NOT NULL UNIQUE,
    email VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_locked BOOLEAN DEFAULT FALSE,
    is_disabled BOOLEAN DEFAULT FALSE,
    is_editor BOOLEAN DEFAULT FALSE,
    require_password_reset BOOLEAN
);

-- Create admins table
CREATE TABLE IF NOT EXISTS cookbook.admins (
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
CREATE TABLE IF NOT EXISTS cookbook.login_attempts (
    id BIGINT GENERATED ALWAYS AS IDENTITY(MINVALUE 1 START WITH 1 CACHE 20) PRIMARY KEY,
    user_id BIGINT UNIQUE,
    admin_id BIGINT UNIQUE,
    attempt_counter INT NOT NULL CHECK(attempt_counter BETWEEN 0 AND 10),
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES cookbook.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_admin FOREIGN KEY(admin_id) REFERENCES cookbook.admins(id) ON DELETE CASCADE
);

CREATE INDEX idx_usr_username ON cookbook.users (username);

CREATE INDEX idx_adm_username ON cookbook.admins (username);

CREATE INDEX idx_log_attmpts ON cookbook.login_attempts (user_id, admin_id, attempt_counter);


-- Create a namespace
CREATE SCHEMA IF NOT EXISTS oauth2;

CREATE TABLE oauth2.oauth2_authorization_consent (
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorities varchar(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name)
);
CREATE TABLE oauth2.oauth2_authorization (
    id varchar(100) NOT NULL,
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorization_grant_type varchar(100) NOT NULL,
    attributes text DEFAULT NULL,
    state varchar(500) DEFAULT NULL,
    authorization_code_value text DEFAULT NULL,
    authorization_code_issued_at timestamp DEFAULT NULL,
    authorization_code_expires_at timestamp DEFAULT NULL,
    authorization_code_metadata text DEFAULT NULL,
    access_token_value text DEFAULT NULL,
    access_token_issued_at timestamp DEFAULT NULL,
    access_token_expires_at timestamp DEFAULT NULL,
    access_token_metadata text DEFAULT NULL,
    access_token_type varchar(100) DEFAULT NULL,
    access_token_scopes varchar(1000) DEFAULT NULL,
    oidc_id_token_value text DEFAULT NULL,
    oidc_id_token_issued_at timestamp DEFAULT NULL,
    oidc_id_token_expires_at timestamp DEFAULT NULL,
    oidc_id_token_metadata text DEFAULT NULL,
    refresh_token_value text DEFAULT NULL,
    refresh_token_issued_at timestamp DEFAULT NULL,
    refresh_token_expires_at timestamp DEFAULT NULL,
    refresh_token_metadata text DEFAULT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE oauth2.oauth2_registered_client (
    id varchar(100) NOT NULL,
    client_id varchar(100) NOT NULL,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret varchar(200) DEFAULT NULL,
    client_secret_expires_at timestamp DEFAULT NULL,
    client_name varchar(200) NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types varchar(1000) NOT NULL,
    redirect_uris varchar(1000) DEFAULT NULL,
    scopes varchar(1000) NOT NULL,
    client_settings varchar(2000) NOT NULL,
    token_settings varchar(2000) NOT NULL,
    PRIMARY KEY (id)
);
