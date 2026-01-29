package com.littlelemon.application.menu.domain.models

data class Dish(
    val title: String,
    val description: String?,
    val price: Double,
    val imageURL: String,
    val stock: Int,
    val nutritionInfo: NutritionInfo?,
    val discountedPrice: Double,
    val category: List<Category>
)

data class NutritionInfo(
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int
)

data class Category(
    val categoryName: String
)