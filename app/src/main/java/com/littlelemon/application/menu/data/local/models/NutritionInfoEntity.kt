package com.littlelemon.application.menu.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NutritionInfoEntity(
    @PrimaryKey val id: Int,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int
)