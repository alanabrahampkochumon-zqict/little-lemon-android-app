package com.littlelemon.application.database.cart.models

import androidx.room.Embedded
import androidx.room.Relation
import com.littlelemon.application.database.menu.models.DishEntity
import com.littlelemon.application.database.menu.models.DishWithCategories

data class CartItemDetails(
    @Embedded val cartItem: CartItemEntity,
    @Relation(entity = DishEntity::class, parentColumn = "dishId", entityColumn = "dishId")
    val dish: DishWithCategories
)
