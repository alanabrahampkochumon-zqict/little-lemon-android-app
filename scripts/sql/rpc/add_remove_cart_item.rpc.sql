CREATE OR REPLACE FUNCTION add_to_cart(
  p_dish_id UUID,
  p_quantity INT
)
RETURNS VOID LANGUAGE plpgsql AS $$
BEGIN
  INSERT INTO cart_item (user_id, dish_id, quantity)
  VALUES (auth.uid(), p_dish_id, p_quantity)
  ON CONFLICT (user_id, dish_id) 
  DO UPDATE SET 
    quantity = cart_item.quantity + EXCLUDED.quantity;
END;
$$;

CREATE OR REPLACE FUNCTION remove_from_cart(
  p_dish_id UUID,
  p_quantity INT
)
RETURNS VOID LANGUAGE plpgsql AS $$
BEGIN
  UPDATE cart_item 
  SET quantity = quantity - p_quantity
  WHERE user_id = auth.uid() AND dish_id = p_dish_id;

  DELETE FROM cart_item 
  WHERE user_id = auth.uid() 
    AND dish_id = p_dish_id 
    AND quantity <= 0;
END;
$$;