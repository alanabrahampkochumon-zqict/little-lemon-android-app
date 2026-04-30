CREATE OR REPLACE FUNCTION get_cart_summary()
RETURNS TABLE(
    subtotal BIGINT,
    taxes BIGINT,
    discount BIGINT,
    shipping BIGINT,
    total_amount BIGINT
) LANGUAGE plpgsql AS $$
DECLARE 
    v_discounted_subtotal BIGINT := 0;
    v_tax_rate NUMERIC;
BEGIN
    -- Calculate both subtotals, multiplying by quantity
    SELECT
        COALESCE(SUM(d.price * c.quantity), 0),
        COALESCE(SUM(
            CASE 
                WHEN d.discounted_price IS NULL OR d.discounted_price = 0 THEN d.price 
                ELSE d.discounted_price 
            END * c.quantity
        ), 0)
    INTO 
        subtotal,
        v_discounted_subtotal
    FROM cart_item c
    JOIN dish d ON c.dish_id = d.id
    WHERE c.user_id = auth.uid();


    IF subtotal = 0 THEN
        taxes := 0;
        discount := 0;
        shipping := 0;
        total_amount := 0;
        RETURN NEXT;
        RETURN;
    END IF;

    -- Fetch restaurant configurations
    SELECT shipping_cost, tax_rate 
    INTO shipping, v_tax_rate 
    FROM restaurant_config 
    LIMIT 1;

    -- Calculate the final values
    discount := subtotal - v_discounted_subtotal;
    taxes := CAST(v_discounted_subtotal * v_tax_rate AS BIGINT);
    total_amount := v_discounted_subtotal + taxes + shipping;

    RETURN NEXT;
END;
$$;