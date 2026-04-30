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
    inner class ToCartDetailItem {

        @Test
        fun mapsToCartItem() {
            val dish = DishGenerator.generateDishWithCategories(1, 2).first().first
            val cartItemEntity = CartItemDetails(CartItemEntity(dish.dish.dishId, 5), dish)

            val result = listOf(cartItemEntity).toCartDetailItems().first()

            assertEquals(dish.toDish(), result.dish)
            assertEquals(cartItemEntity.cartItem.quantity, result.quantity)
        }
    }

    @Nested
    inner class ToCartItem {

        @Test
        fun mapsToCartItem() {
            val expectedId = "abcd-1324"
            val expectedQuantity = 5
            val cartItemEntity = CartItemEntity(expectedId, expectedQuantity)

            val cartItem = listOf(cartItemEntity).toCartItems().first()

            assertEquals(expectedId, cartItem.dishId)
            assertEquals(expectedQuantity, cartItem.quantity)
        }
    }

}