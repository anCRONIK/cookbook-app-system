--liquibase formatted sql
--changeset Nikola Presecki:db-init

CREATE INDEX idx_usr_username ON cookbook.users (username);

CREATE INDEX idx_adm_username ON cookbook.admins (username);

CREATE INDEX idx_log_attmpts ON cookbook.login_attempts (user_id, admin_id, attempt_counter);