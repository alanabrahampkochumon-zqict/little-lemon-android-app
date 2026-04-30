package com.littlelemon.application.shared.cart.data.remote.mappers

import com.littlelemon.application.shared.cart.data.remote.mappers.toEntity
import com.littlelemon.application.shared.cart.data.remote.models.CartItemDTO
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CartItemDTOMapperTests {

    @Test
    fun toEntity_correctlyMapsToEntity() {
        val dishId = Uuid.generateV4().toString()
        val quantity = 5

        val entity = CartItemDTO(dishId, quantity).toEntity()

        assertEquals(dishId, entity.dishId)
        assertEquals(quantity, entity.quantity)
    }

}