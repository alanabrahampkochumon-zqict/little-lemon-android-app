CREATE TYPE RESERVATION_STATUS AS ENUM ('confirmed', 'cancelled', 'completed');

CREATE TABLE
    IF NOT EXISTS reservations (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        user_id UUID NOT NULL REFERENCES auth.users (id) ON DELETE CASCADE DEFAULT auth.uid (),
        reservation_time TIMESTAMPTZ NOT NULL,
        party_size INT CHECK (party_size > 0),
        status RESERVATION_STATUS DEFAULT 'confirmed',
        special_instructions TEXT
    );

CREATE POLICY "Users can manage their own cart reservations" ON reservations FOR ALL USING (auth.uid () = user_id);