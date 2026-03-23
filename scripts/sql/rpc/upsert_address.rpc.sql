CREATE OR REPLACE FUNCTION get_time_millis()
RETURNS bigint
LANGUAGE sql
STABLE
AS $$
  SELECT (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint;
$$;

CREATE OR REPLACE FUNCTION upsert_address(
  arg_location geography (POINT),
  arg_created_at timestamptz,
  arg_is_default boolean DEFAULT false,
  arg_id uuid DEFAULT NULL,
  arg_label text DEFAULT NULL,
  arg_building_name text DEFAULT NULL,
  arg_street_address text DEFAULT NULL,
  arg_city text DEFAULT NULL,
  arg_state text DEFAULT NULL,
  arg_pin_code text DEFAULT NULL
)
RETURNS SETOF user_address AS $$
DECLARE
  final_default_state boolean;
  existing_count int;
  final_id uuid;
BEGIN
    SELECT count(*) INTO existing_count
    FROM user_address
    WHERE user_id = auth.uid();
    
    final_id := COALESCE(arg_id, gen_random_uuid());

    -- If user's first address, then make it default
    IF existing_count = 0 THEN
        final_default_state := true;
    ELSE
        final_default_state := arg_is_default;
    END IF;

    -- If the currently updating address is default, then make the other ones non-default
    IF final_default_state = true THEN
        UPDATE user_address
        SET is_default = false
        WHERE user_id = auth.uid();
    END IF;

    INSERT INTO user_address (
        id,
        user_id,
        label,
        address,
        street_address,
        city,
        state,
        pin_code,
        is_default,
        location,
        created_at

    )
    VALUES (
        final_id,
        auth.uid(),
        arg_label,
        arg_building_name,
        arg_street_address,
        arg_city,
        arg_state,
        arg_pin_code,
        final_default_state,
        arg_location,
        arg_created_at
    )
    ON CONFLICT (id) DO UPDATE SET
        label = EXCLUDED.label,
        address = EXCLUDED.address,
        street_address = EXCLUDED.street_address,
        city = EXCLUDED.city,
        state = EXCLUDED.state,
        pin_code = EXCLUDED.pin_code,
        is_default = EXCLUDED.is_default,
        location = EXCLUDED.location,
        created_at = EXCLUDED.created_at;
    
    RETURN QUERY SELECT * FROM user_addresses_view WHERE id = final_id;
END;
$$ LANGUAGE plpgsql;