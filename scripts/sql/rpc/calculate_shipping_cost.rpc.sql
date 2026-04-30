CREATE OR REPLACE FUNCTION get_order_summary
RETURNS TABLE(
    subtotal BIGINT,
    taxes BIGINT,
    discount BIGINT,
    shipping BIGINT,
    total_amount BIGINT
)
AS $$
DECLARE discounted_subtotal BIGINT
DECLARE non_discounted_subtotal BIGINT
DECLARE total_quantity INT
BEGIN
    SELECT
        SUM(quantity) INTO total_quantity,
        SUM(d.price) INTO non_discounted_subtotal,
        SUM(CASE WHEN d.discounted_price = 0 THEN d.price ELSE d.discounted_price) INTO discounted_subtotal,
    FROM cart_item WHERE user_id = auth.uid()
    JOIN dish d ON cart_item.dish_id = d.id;
    SELECT 
END;
$$;