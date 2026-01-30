package com.littlelemon.application.menu.data.mappers

import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.mappers.utils.convertCentsToDollars
import com.littlelemon.application.menu.data.remote.models.CategoryDTO
import com.littlelemon.application.menu.data.remote.models.DishDTO
import com.littlelemon.application.menu.data.remote.models.NutritionInfoDTO

data class MenuDataBundle(
    val dishes: List<DishDTO>,
    val categories: List<CategoryDTO>,
    val crossRef: List<DishCategoryCrossRef>
)

fun List<DishDTO>.toDishWithCategories(): MenuDataBundle {
    val result = this.map { dishDTO ->
        val dishEntity = DishEntity(
            title = dishDTO.title,
            description = dishDTO.description ?: "",
            price = convertCentsToDollars(dishDTO.price),
            image = dishDTO.image,
            stock = dishDTO.stock,
            nutritionInfo = dishDTO.nutritionInfo?.toNutritionInfoEntity(),
            discountedPrice = convertCentsToDollars(dishDTO.discountedPrice),
            popularityIndex = dishDTO.popularityIndex,
            dateAdded = dishDTO.dateAdded
        )
        val categories = dishDTO.categories.map { categoryDTO ->
            CategoryEntity(categoryName = categoryDTO.categoryName)
        }
//        val crossRef = dishDTO.
        // TODO: Update Seeder, database, and tests to use long instead of double for discounted price
        // TODO: Change DishEntity.Nutrition to DishEntity.NutritionInfo

//        return Triple(dishEntity, categories, crossRef)
    }
    return MenuDataBundle(
        dishes = emptyList(),
        categories = emptyList(),
        crossRef = emptyList()
    )
}

private fun NutritionInfoDTO.toNutritionInfoEntity(): DishEntity.Nutrition = DishEntity.Nutrition(
    calories = this.calories,
    protein = this.protein,
    carbs = this.carbs,
    fats = this.fats
)

