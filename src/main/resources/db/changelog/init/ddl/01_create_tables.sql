--liquibase formatted sql
--changeset Nikola Presecki:db-init

-- Create a keyspace
CREATE KEYSPACE IF NOT EXISTS cookbook WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' };

-- create user types
CREATE TYPE cookbook.ingredient (
   name VARCHAR,
   quantity VARCHAR,
   measurement_unit VARCHAR,
);

-- Create authors table
CREATE TABLE IF NOT EXISTS cookbook.authors (
    username VARCHAR PRIMARY KEY,
    full_name VARCHAR,
    date_of_birth DATE,
    bio TEXT,
    image_url VARCHAR,
);

-- Create recipes table
CREATE TABLE IF NOT EXISTS cookbook.recipes (
    id BIGINT,
    title VARCHAR,
    short_description TEXT,
    cover_image_url VARCHAR,
    ingredients LIST<frozen<ingredient>>,
    preparation_time TINYINT,
    preparation_instructions TEXT,
    cooking_time TINYINT,
    cooking_instructions TEXT,
    date_created TIMESTAMP,
    date_last_updated TIMESTAMP,
    difficulty TINYINT,
    category VARCHAR,
    rating FLOAT,
    author_username VARCHAR,
    PRIMARY KEY (author_username, id)
);

-- Create recipe comments table
CREATE TABLE IF NOT EXISTS cookbook.recipe_comments (
    recipe_id BIGINT,
    username VARCHAR,
    text TEXT,
    date_created TIMESTAMP,
    PRIMARY KEY (recipe_id, username, date_created)
);