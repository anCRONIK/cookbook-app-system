--liquibase formatted sql
--changeset Nikola Presecki:db-init

-- WEIGHT
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('kg', 'kg', 'weight', false, true);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('dg', 'dg', 'weight', false, true);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('g', 'g', 'weight', false, true);

INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('lb', 'lb', 'weight', true, false);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('oz', 'oz', 'weight', true, false);

-- VOLUME
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('l', 'l', 'volume', false, true);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('dl', 'dl', 'volume', false, true);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('ml', 'ml', 'volume', false, true);

INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('gal', 'gal', 'volume', true, false);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('pt', 'pt', 'volume', true, false);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('fl oz', 'fl oz', 'volume', true, false);

INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('cup', 'cup', 'volume', true, true);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('spoon', 'spoon', 'volume', true, true);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('teaspoon', 'teaspoon', 'volume', true, true);

-- LENGTH
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('m', 'm', 'length', false, true);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('cm', 'cm', 'length', false, true);

INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('mi', 'mi', 'length', true, false);
INSERT INTO cookbook.measurement_units(name, code, category, is_imperial, is_metric) VALUES ('inch', 'inch', 'length', true, false);