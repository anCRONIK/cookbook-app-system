CREATE KEYSPACE IF NOT EXISTS cookbook WITH replication = {'class':'SimpleStrategy','replication_factor':'1'};

-- create user types
CREATE TYPE cookbook.ingredient (
   name VARCHAR,
   quantity VARCHAR,
   measurement_unit VARCHAR
);

-- Create authors table
CREATE TABLE IF NOT EXISTS cookbook.authors (
    username VARCHAR PRIMARY KEY,
    full_name VARCHAR,
    date_of_birth DATE,
    bio TEXT,
    image_url VARCHAR
);

-- Create recipes table
CREATE TABLE IF NOT EXISTS cookbook.recipes (
    id BIGINT PRIMARY KEY,
    title VARCHAR,
    short_description TEXT,
    thumbnail_url VARCHAR,
    cover_image_url VARCHAR,
    ingredients LIST<frozen<ingredient>>,
    preparation_time INT,
    preparation_instructions TEXT,
    cooking_time INT,
    cooking_instructions TEXT,
    date_created TIMESTAMP,
    date_last_updated TIMESTAMP,
    difficulty INT,
    category VARCHAR,
    rating FLOAT,
    author_username VARCHAR
);

-- Create recipe categories table
CREATE TABLE IF NOT EXISTS cookbook.recipe_categories (
    category VARCHAR,
    PRIMARY KEY (category)
);

-- Create recipe comments table
CREATE TABLE IF NOT EXISTS cookbook.recipe_comments (
    recipe_id BIGINT,
    username VARCHAR,
    text TEXT,
    date_created TIMESTAMP,
    PRIMARY KEY (recipe_id, date_created, username)
) WITH CLUSTERING ORDER BY (date_created DESC, username ASC);

-- Create measurement units table
CREATE TABLE IF NOT EXISTS cookbook.measurement_units (
    name VARCHAR,
    code VARCHAR,
    category VARCHAR,
    is_imperial BOOLEAN,
    is_metric BOOLEAN,
    PRIMARY KEY (code)
);


-- ######################################### INDEXES #########################################
CREATE INDEX ON cookbook.recipes(rating);
CREATE INDEX ON cookbook.recipes(category);

CREATE INDEX ON cookbook.measurement_units(is_imperial);
CREATE INDEX ON cookbook.measurement_units(is_metric);

-- ######################################### DATA #########################################

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

-- RECIPE CATEGORIES
INSERT INTO cookbook.recipe_categories(category) VALUES ('appetizer');
INSERT INTO cookbook.recipe_categories(category) VALUES ('entree');
INSERT INTO cookbook.recipe_categories(category) VALUES ('dessert');