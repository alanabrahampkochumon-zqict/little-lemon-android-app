package com.littlelemon.application.menu.utils

import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity
import io.github.serpro69.kfaker.faker
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.time.Clock

private const val FOUR_YEARS_IN_MILLIS = 4 * 365 * 12 * 30 * 24 * 60 * 60 * 1000L

class MenuEntityGenerator {
    private val faker = faker {}
    fun generateDishEntity(): DishEntity {

        val nutrition = DishEntity.Nutrition(
            calories = (Math.random() * 1000).roundToInt(),
            protein = (Math.random() * 1000).roundToInt(),
            carbs = (Math.random() * 1000).roundToInt(),
            fats = (Math.random() * 1000).roundToInt(),
        )

        val timeNowMillis = Clock.System.now().toEpochMilliseconds()

        return DishEntity(
            dishId = Clock.System.now().toEpochMilliseconds(),
            title = faker.dessert.dessert()(),
            description = faker.lorem.words(),
            price = Math.random() * 1000,
            image = faker.internet.domain(subdomain = true),
            stock = (Math.random() * 1000).roundToInt(),
            nutritionInfo = nutrition,
            discountedPrice = Math.random() * 1000,
            popularityIndex = (0..100).random(),
            dateAdded = (timeNowMillis - Math.random() * FOUR_YEARS_IN_MILLIS).roundToLong() // TODO: Update
        )
    }

    fun generateCategoryEntities(numCategories: Int = 1): List<CategoryEntity> {
        val categories = mutableListOf<CategoryEntity>()
        repeat(numCategories) {
            categories.add(
                CategoryEntity(
                    categoryId = it.toLong(),
                    categoryName = faker.adjective.positive()
                )
            )
        }
        return categories
    }

    // Add Random Number of category associations to dish and create a cross reference.
    fun generateDishCategoryCrossRef(
        dish: DishEntity,
        categories: List<CategoryEntity>
    ): List<DishCategoryCrossRef> {
        val categoriesToInsert =
            categories.shuffled().drop((Math.random() * categories.size).roundToInt())
        return categoriesToInsert.map { (categoryId, _) ->
            DishCategoryCrossRef(
                dishId = dish.dishId!!,
                categoryId = categoryId!!
            )
        }
    }
}
