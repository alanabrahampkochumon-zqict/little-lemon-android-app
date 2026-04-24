package com.littlelemon.application.cart.data.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.littlelemon.application.menu.data.local.models.DishEntity

data class CartItemDetails(
    @Embedded val cartItem: CartItemEntity,
    @Relation(parentColumn = "dishId", entityColumn = "dishId")
    val dish: DishEntity
)
