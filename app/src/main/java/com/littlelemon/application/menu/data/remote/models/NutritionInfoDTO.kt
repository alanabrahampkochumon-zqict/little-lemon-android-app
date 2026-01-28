package com.littlelemon.application.menu.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class NutritionInfoDTO(
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int
)
