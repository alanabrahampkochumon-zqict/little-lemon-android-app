package com.littlelemon.application.menu.data.mappers

import com.littlelemon.application.core.domain.utils.toLocalDateTime
import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.local.models.DishWithCategories
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo

fun DishWithCategories.toDish(): Dish {
    return Dish(
        title = dish.title,
        description = dish.description,
        price = dish.price,
        imageURL = dish.image,
        stock = dish.stock,
        nutritionInfo = dish.nutritionInfo?.toDomainNutritionInfo(),
        discountedPrice = dish.discountedPrice,
        popularityIndex = dish.popularityIndex,
        dateAdded = dish.dateAdded.toLocalDateTime(),
        category = categories.toDomainCategories()
    )
}

private fun DishEntity.NutritionInfo.toDomainNutritionInfo(): NutritionInfo {
    return NutritionInfo(
        calories = calories,
        protein = protein,
        fats = fats,
        carbs = carbs
    )
}

private fun List<CategoryEntity>.toDomainCategories(): List<Category> =
    this.map { (_, categoryName) ->
        Category(categoryName = categoryName)
    }
