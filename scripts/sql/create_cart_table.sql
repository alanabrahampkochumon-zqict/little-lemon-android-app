CREATE TABLE
    IF NOT EXISTS user_cart (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        user_id UUID NOT NULL REFERENCES auth.users (id) ON DELETE CASCADE DEFAULT auth.uid (),
    );