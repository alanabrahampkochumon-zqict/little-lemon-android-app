package com.littlelemon.application.menu.data.local.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DishWithCategories(
    @Embedded val dish: DishEntity,
    @Relation(
        parentColumn = "dishId",
        entityColumn = "categoryId",
        associateBy = Junction(DishCategoryCrossRef::class)
    )
    val categories: List<CategoryEntity>
)