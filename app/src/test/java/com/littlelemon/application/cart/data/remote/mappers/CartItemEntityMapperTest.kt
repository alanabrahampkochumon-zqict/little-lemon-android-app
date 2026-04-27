package com.littlelemon.application.cart.data.remote.mappers

import com.littlelemon.application.database.cart.models.CartItemEntity
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CartItemEntityMapperTest {
    @Test
    fun toDTO_correctlyMapsToDTO() {
        val dishId = Uuid.generateV4().toString()
        val quantity = 5

        val dto = CartItemEntity(dishId, quantity).toDTO()

        assertEquals(dishId, dto.dishId)
        assertEquals(quantity, dto.quantity)
    }
}