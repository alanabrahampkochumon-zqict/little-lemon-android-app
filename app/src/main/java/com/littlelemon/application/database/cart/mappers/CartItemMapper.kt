package com.littlelemon.application.database.cart.mappers

import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.database.cart.models.CartItemEntity

fun CartItem.toEntity() = CartItemEntity(dish.id, quantity, id)