-- NOTE: Enable postgis extension from the database.
-- https://supabase.com/docs/guides/database/extensions/postgis
CREATE TABLE
    IF NOT EXISTS user_address (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        user_id UUID NOT NULL REFERENCES auth.users (id) ON DELETE CASCADE DEFAULT auth.uid (),
        label TEXT,
        address TEXT,
        street_address TEXT,
        city VARCHAR(50),
        state VARCHAR(50),
        pin_code VARCHAR(20),
        location geography (POINT),
        created_at TIMESTAMP DEFAULT now ()
    );

-- RLS to prevent users from creating more than 5 addresses
ALTER TABLE user_address ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Limit addresses per user" ON user_address FOR INSERT TO authenticated
WITH
    CHECK (
        (
            SELECT
                count(*)
            FROM
                user_address
            WHERE
                user_id = auth.uid ()
        ) < 5
    );

CREATE POLICY "User can see their own addresses" ON user_address FOR
SELECT
    USING (auth.uid () = user_id);

CREATE VIEW user_addresses_view AS
SELECT
  user_address.id,
  user_address.label,
  user_address.address,
  user_address.street_address,
  user_address.city,
  user_address.pin_code,
  -- Extract Lat/Long from the PostGIS 'location' column
  st_y(location::geometry) as latitude,
  st_x(location::geometry) as longitude,
  user_address.created_at
FROM user_address;
