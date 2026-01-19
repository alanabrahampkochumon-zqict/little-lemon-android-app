package com.littlelemon.application.menu.data.local.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["dishId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = DishEntity::class,
            parentColumns = ["dishId"],
            childColumns = ["dishId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class DishCategoryCrossRef(
    val dishId: Long,
    val categoryId: Long
)
