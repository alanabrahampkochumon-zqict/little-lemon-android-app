package com.littlelemon.application.menu.data.local.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DishEntity(
    @PrimaryKey(autoGenerate = true) val dishId: Long? = null,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    val stock: Int,
    @Embedded val nutritionInfo: Nutrition?,
    val discountedPrice: Double,
) {
    data class Nutrition(
        val calories: Int,
        val protein: Int,
        val carbs: Int,
        val fats: Int
    )
}


