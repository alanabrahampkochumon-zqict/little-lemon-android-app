package com.littlelemon.application.core.data.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.littlelemon.application.menu.data.local.models.DishEntity

data class CartItemDetails(
    @Embedded val cartItem: CartItemEntity,
    @Relation(parentColumn = "dishId", entityColumn = "id")
    val dish: DishEntity
)
