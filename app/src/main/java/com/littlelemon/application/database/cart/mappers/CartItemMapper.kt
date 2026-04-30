package com.littlelemon.application.database.cart.mappers

import com.littlelemon.application.database.cart.models.CartItemEntity
import com.littlelemon.application.shared.cart.data.remote.models.CartItemDTO
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem

fun CartDetailItem.toEntity() = CartItemEntity(dish.id, quantity)

fun CartDetailItem.toDTO() = CartItemDTO(dish.id, quantity)