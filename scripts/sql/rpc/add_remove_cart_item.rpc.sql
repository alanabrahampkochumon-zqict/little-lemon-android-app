CREATE OR REPLACE FUNCTION sync_cart_item(
  p_dish_id UUID,
  p_quantity INT
)
RETURNS cart_item LANGUAGE plpgsql AS $$
DECLARE
    d_cart_item cart_item;
BEGIN
    -- If the quantity is less than or equal to 0, then delete it from database and return null
    IF p_quantity <= 0 THEN 
        DELETE FROM cart_item WHERE user_id = auth.uid() AND dish_id = p_dish_id;
        RETURN NULL;
    END IF;
  
    -- If quantity is non-negative, either increment the count or insert into database if the record doesn't exist.
    INSERT INTO cart_item (user_id, dish_id, quantity)
    VALUES (auth.uid(), p_dish_id, p_quantity)
    ON CONFLICT (user_id, dish_id) 
    DO UPDATE SET quantity = EXCLUDED.quantity
    RETURNING * INTO d_cart_item;
    
    RETURN d_cart_item;
END;
$$;

CREATE OR REPLACE FUNCTION clear_cart()
RETURNS VOID LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM cart_item
    WHERE user_id = auth.uid();
END;
$$;