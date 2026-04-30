CREATE TABLE
    IF NOT EXISTS restaurant_config (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        location_name VARCHAR(255),
        address TEXT,
        lat_lng geography (POINT),
        opening_time TIME,
        closing_time TIME,
        service_radius_miles float,
        shipping_cost BIGINT,
        tax_rate NUMERIC
    );

INSERT INTO
    restaurant_config (
        location_name,
        address,
        lat_lng,
        opening_time,
        closing_time,
        service_radius_miles,
        shipping_cost,
        tax_rate
    )
VALUES
    (
        'Little Lemon Chicago',
        'Little Lemon, 2407 Vesta Drive, Illinois, Chicago-60626',
        'POINT(-87.667010 41.903794)',
        '10:00:00',
        '22:00:00',
        10.0,
        0,
        0.18
    );