DROP TYPE IF EXISTS cat_color CASCADE;
CREATE TYPE cat_color AS ENUM (
    'black',
    'white',
    'black & white',
    'red',
    'red & white',
    'red & black & white'
);

DROP TABLE IF EXISTS cats CASCADE;
CREATE TABLE cats (
    name varchar,
    color cat_color,
    tail_length int,
    whiskers_length int,
    PRIMARY KEY (name)
);

DROP TABLE IF EXISTS cat_colors_info CASCADE;
CREATE TABLE cat_colors_info (
    color cat_color UNIQUE,
    count int
);

DROP TABLE IF EXISTS cats_stat CASCADE;
CREATE TABLE cats_stat (
    tail_length_mean numeric,
    tail_length_median numeric,
    tail_length_mode integer[],
    whiskers_length_mean numeric,
    whiskers_length_median numeric,
    whiskers_length_mode integer[]
);

DROP TABLE IF EXISTS array_table CASCADE;
CREATE TABLE array_table (
    name varchar,
	surname varchar,
    tail_length_mode integer[]
);

