CREATE TYPE ORDER_STATUS as ENUM (
    'pending',
    'confirmed',
    'preparing',
    'out_for_delivery',
    'delivered',
    'cancelled'
);

CREATE TABLE
    IF NOT EXISTS user_orders (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        user_id UUID NOT NULL REFERENCES auth.users (id) ON DELETE CASCADE DEFAULT auth.uid (),
        label TEXT NOT NULL,
        order_date TIMESTAMPTZ DEFAULT now (),
        updated_at TIMESTAMPTZ DEFAULT now (),
        status ORDER_STATUS DEFAULT 'pending',
        payment_mode VARCHAR(50) NOT NULL,
        deliver_to UUID NOT NULL REFERENCES public.user_address (id) ON DELETE CASCADE,
        bill_amount BIGINT NOT NULL,
        delivery_charge BIGINT NOT NULL,
        discount BIGINT NOT NULL,
        total_payable BIGINT NOT NULL,
        refunded_on TIMESTAMPTZ DEFAULT NULL,
        special_instructions TEXT
    );

---
CREATE TABLE
    IF NOT EXISTS menu_order_item (
        order_id UUID NOT NULL REFERENCES public.user_orders (id) ON DELETE CASCADE,
        dish_id UUID NOT NULL REFERENCES public.dish (id) ON DELETE SET NULL,
        dish_name_snapshot VARCHAR(255) NOT NULL,
        dish_price_snapshot BIGINT NOT NULL,
        order_quantity INTEGER NOT NULL DEFAULT 1,
        PRIMARY KEY (order_id, dish_id)
    );

CREATE POLICY "User can see their own orders" ON user_orders FOR
SELECT
    USING (auth.uid () = user_id);