package com.littlelemon.application.database.menu.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class DishEntity(
    @PrimaryKey(autoGenerate = false) val dishId: String = UUID.randomUUID()
        .toString(),
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    val stock: Int,
    @Embedded val nutritionInfo: NutritionInfo?,
    val discountedPrice: Double,
    val popularityIndex: Int = 0, // Used for categorizing products with best sales
    val dateAdded: String
) {
    data class NutritionInfo(
        @ColumnInfo(name = "calories") val calories: Int,
        @ColumnInfo(name = "protein") val protein: Int,
        @ColumnInfo(name = "carbs") val carbs: Int,
        @ColumnInfo(name = "fats") val fats: Int
    )
}


