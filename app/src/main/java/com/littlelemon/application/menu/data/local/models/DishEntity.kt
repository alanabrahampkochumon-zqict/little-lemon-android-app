package com.littlelemon.application.menu.data.local.models

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class DishEntity(
    @param:NonNull @PrimaryKey(autoGenerate = false) val dishId: String = UUID.randomUUID()
        .toString(),
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    val stock: Int,
    @Embedded val nutritionInfo: NutritionInfo?,
    val discountedPrice: Double,
    val popularityIndex: Int = 0, // Used for categorizing products with best sales
    val dateAdded: Long
) {
    data class NutritionInfo(
        val calories: Int,
        val protein: Int,
        val carbs: Int,
        val fats: Int
    )
}


