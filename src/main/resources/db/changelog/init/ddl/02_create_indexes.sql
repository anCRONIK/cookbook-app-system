--liquibase formatted sql
--changeset Nikola Presecki:db-init

CREATE INDEX ON cookbook.recipes(rating);
CREATE INDEX ON cookbook.recipes(category);