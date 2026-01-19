package com.littlelemon.application.menu.domain.models

import com.littlelemon.application.menu.data.local.models.DishEntity

data class Dish(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    val stock: Int,
    val nutritionInfo: DishEntity.Nutrition,
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