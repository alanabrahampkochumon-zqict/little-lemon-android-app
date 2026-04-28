package com.littlelemon.application.menu.data.mappers

import com.littlelemon.application.database.menu.models.CategoryEntity
import com.littlelemon.application.database.menu.models.DishEntity
import com.littlelemon.application.database.menu.models.DishWithCategories
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun DishWithCategories.toDish(): Dish {
    val instant = Instant.parse(dish.dateAdded)
    val timezone = TimeZone.currentSystemDefault()
    return Dish(
        id = dish.dishId,
        title = dish.title,
        description = dish.description,
        price = dish.price,
        imageURL = dish.image,
        stock = dish.stock,
        nutritionInfo = dish.nutritionInfo?.toDomainNutritionInfo(),
        discountedPrice = if (dish.discountedPrice < 0.001) null else dish.discountedPrice,
        popularityIndex = dish.popularityIndex,
        dateAdded = instant.toLocalDateTime(timezone),
        category = categories.map { it.toDomainCategory() }
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

fun CategoryEntity.toDomainCategory(): Category =
    Category(categoryName = categoryName)

