package com.littlelemon.application.menu.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DishDTO(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    val stock: Int,
    @SerialName("nutrition_info")
    val nutritionInfo: NutritionInfo,
    @SerialName("discounted_price")
    val discountedPrice: Double,
    val category: List<Category>
)

@Serializable
data class NutritionInfo(
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int
)

@Serializable
data class Category(
    @SerialName("category_name")
    val categoryName: String
)