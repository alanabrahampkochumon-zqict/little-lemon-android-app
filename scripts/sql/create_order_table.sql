CREATE TABLE
    IF NOT EXISTS user_orders (
        id UUID PRIMARY KEY DEFAULT get_random_uuid (),
        user_id UUID NOT NULL REFERENCES auth.users (id) ON DELETE CASCADE DEFAULT auth.uid (),
        label TEXT NOT NULL,
        order_date TIMESTAMP DEFAULT now (),
        updated_at TIMESTAMP DEFAULT now (),
        status ENUM ("ordered", "delivered", "cancelled") DEFAULT "ordered",
        payment_mode VARCHAR(50) NOT NULL,
        delivered_to UUID NOT NULL REFERENCES user_address.id ON DELETE CASCADE,
        bill_amount BIGINT NOT NULL,
        delivery_charge BIGINT NOT NULL,
        discount BIGINT NOT NULL,
        total_payable BIGINT NOT NULL,
        refunded_on TIMESTAMP DEFAULT NULL,
        special_instructions TEXT,
    );

---
CREATE TABLE
    IF NOT EXISTS menu_order_item (
        order_id UUID NOT NULL REFERENCES user_orders.id ON DELETE CASCADE,
        dish_id UUID NOT NULL REFERENCES dish.id ON DELETE SET NULL,
        dish_name_snapshot VARCHAR(255) NOT NULL,
        dish_price_snapshot BIGINT NOT NULL,
        order_quantity INTEGER NOT NULL DEAFULT 1,
        PRIMARY KEY (order_id, dish_id)
    );

CREATE POLICY "User can see their own orders" ON user_orders FOR
SELECT
    USING (auth.uid () = user_id);

CREATE OR REPLACE FUNCTION upd_timestamp() RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.modified = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;

CREATE TRIGGER t_name
  BEFORE UPDATE
  ON tablename
  FOR EACH ROW
  EXECUTE PROCEDURE upd_timestamp();