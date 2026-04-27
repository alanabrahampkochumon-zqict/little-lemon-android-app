package com.littlelemon.application.database.cart.mappers

import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.menu.utils.DishGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CartItemMapperTests {


    private val dish = DishGenerator.generateDish()

    @OptIn(ExperimentalUuidApi::class)
    private val cartItemId = Uuid.generateV4().toString()
    private val quantity = 5

    @Nested
    inner class ToEntity {

        @Test
        fun mapsToEntity() {
            val entity = CartItem(cartItemId, dish, quantity).toEntity()

            assertEquals(quantity, entity.quantity)
            assertEquals(dish.id, entity.dishId)
            assertEquals(cartItemId, entity.id)
        }
    }
}