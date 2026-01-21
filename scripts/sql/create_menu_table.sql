-- Use for Table Deletion if necessary
-- DROP TABLE dish_category;
-- DROP TABLE dish;
-- DROP TABLE nutrition_info;
-- DROP TABLE category;
--
-- Table Creation Queries
CREATE TABLE
    IF NOT EXISTS nutrition_info (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        calories INTEGER NOT NULL,
        protein INTEGER NOT NULL,
        carbs INTEGER NOT NULL,
        fats INTEGER NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS category (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        category_name VARCHAR(255) NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS dish (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        title VARCHAR(255) NOT NULL,
        description TEXT,
        price BIGINT NOT NULL,
        stock INTEGER NOT NULL DEFAULT 0,
        popularity_index INTEGER NOT NULL CHECK (
            popularity_index > 0
            AND popularity_index <= 100
        ) DEFAULT 1,
        date_added TIMESTAMP NOT NULL DEFAULT NOW (),
        nutrition_info_id UUID REFERENCES nutrition_info (id)
    );

CREATE TABLE
    IF NOT EXISTS dish_category (
        dish_id UUID references dish (id) ON DELETE CASCADE,
        category_id UUID references category (id) ON DELETE SET NULL,
        PRIMARY KEY (dish_id, category_id)
    );