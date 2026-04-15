CREATE OR REPLACE FUNCTION make_reservation (
    p_reservation_time TIMESTAMPTZ,
    p_party_size INT,
    p_special_instructions TEXT
) RETURNS reservations AS $$
DECLARE
    new_reservation reservations;
BEGIN
    INSERT INTO reservations(id, user_id, reservation_time, party_size, status, special_instructions)
    VALUES(gen_random_uuid(), auth.uid(), p_reservation_time, p_party_size, 'confirmed', p_special_instructions)
    RETURNING * INTO new_reservation;

    RETURN new_reservation;
END;
$$ LANGUAGE plpgsql;
