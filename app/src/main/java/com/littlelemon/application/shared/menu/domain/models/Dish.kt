package com.littlelemon.application.shared.menu.domain.models

import kotlinx.datetime.LocalDateTime

data class Dish(
    val id: String,
    val title: String,
    val description: String?,
    val price: Double,
    val imageURL: String,
    val stock: Int,
    val nutritionInfo: NutritionInfo?,
    val discountedPrice: Double?,
    val category: List<Category>,
    val dateAdded: LocalDateTime,
    val popularityIndex: Int,
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