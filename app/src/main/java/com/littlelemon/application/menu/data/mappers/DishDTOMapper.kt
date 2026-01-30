package com.littlelemon.application.menu.data.mappers

import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.mappers.utils.convertCentsToDollars
import com.littlelemon.application.menu.data.remote.models.DishDTO
import com.littlelemon.application.menu.data.remote.models.NutritionInfoDTO

data class MenuDataBundle(
    val dishes: List<DishEntity>,
    val categories: List<CategoryEntity>,
    val crossRef: List<DishCategoryCrossRef>
)

fun List<DishDTO>.toDishWithCategories(): MenuDataBundle {
    val dishes = mutableListOf<DishEntity>()
    val categories = mutableListOf<CategoryEntity>()
    val crossRefs = mutableListOf<DishCategoryCrossRef>()
    val addedCategoryId = mutableSetOf<String>()
    this.forEach { dishDTO ->
        dishes.add(
            DishEntity(
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
        )
        dishDTO.categories.forEach { (categoryId, categoryName) ->
            if (categoryId !in addedCategoryId) {
                addedCategoryId.add(categoryId)
                categories.add(CategoryEntity(categoryId = categoryId, categoryName = categoryName))
                crossRefs.add(DishCategoryCrossRef(dishDTO.id, categoryId))
            }
        }

    }
    return MenuDataBundle(
        dishes = dishes,
        categories = categories,
        crossRef = crossRefs
    )
}

private fun NutritionInfoDTO.toNutritionInfoEntity(): DishEntity.NutritionInfo =
    DishEntity.NutritionInfo(
        calories = this.calories,
        protein = this.protein,
        carbs = this.carbs,
        fats = this.fats
    )

