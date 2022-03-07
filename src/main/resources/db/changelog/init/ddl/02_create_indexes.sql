--liquibase formatted sql
--changeset Nikola Presecki:db-init

CREATE INDEX ON cookbook.recipes(rating);
CREATE INDEX ON cookbook.recipes(category);

CREATE INDEX ON cookbook.measurement_units(is_imperial);
CREATE INDEX ON cookbook.measurement_units(is_metric);