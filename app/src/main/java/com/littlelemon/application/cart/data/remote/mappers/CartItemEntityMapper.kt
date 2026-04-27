package com.littlelemon.application.cart.data.remote.mappers

import com.littlelemon.application.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.database.cart.models.CartItemEntity

fun CartItemEntity.toDTO() = CartItemDTO(dishId, quantity)