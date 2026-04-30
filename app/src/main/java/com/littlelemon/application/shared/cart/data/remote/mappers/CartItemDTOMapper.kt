package com.littlelemon.application.shared.cart.data.remote.mappers

import com.littlelemon.application.shared.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.database.cart.models.CartItemEntity

fun CartItemDTO.toEntity() = CartItemEntity(dishId, quantity)