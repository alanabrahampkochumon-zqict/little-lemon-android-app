CREATE TABLE
    IF NOT EXISTS cart_item (
        user_id UUID NOT NULL REFERENCES auth.users (id) ON DELETE CASCADE DEFAULT auth.uid (),
        dish_id UUID NOT NULL REFERENCES public.dish (id) ON DELETE CASCADE,
        quantity INTEGER NOT NULL CHECK (quantity > 0) DEFAULT 1,
        PRIMARY KEY (user_id, dish_id)
    );

ALTER TABLE cart_item ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can manage their own cart items" ON cart_item FOR ALL USING (auth.uid () = user_id);