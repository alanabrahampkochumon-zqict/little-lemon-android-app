package com.littlelemon.application.menu.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DishEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    val stock: Int,
    val nutritionInfo: NutritionInfoEntity,
    val discountedPrice: Double,
    val category: List<CategoryEntity>
)


