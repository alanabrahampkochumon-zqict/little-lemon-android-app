package com.littlelemon.application.database.cart.mappers

import com.littlelemon.application.database.cart.models.CartItemDetails
import com.littlelemon.application.database.cart.models.CartItemEntity
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.menu.data.mappers.toDish
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CartEntityMapperTests {

    @Nested
    inner class ToCartItem {

        @Test
        fun mapsToCartItem() {
            val dish = DishGenerator.generateDishWithCategories(1, 2).first().first
            val cartItemEntity = CartItemDetails(CartItemEntity(dish.dish.dishId, 5), dish)

            val result = listOf(cartItemEntity).toCartItems().first()

            assertEquals(dish.toDish(), result.dish)
            assertEquals(cartItemEntity.cartItem.quantity, result.quantity)
        }
    }

}