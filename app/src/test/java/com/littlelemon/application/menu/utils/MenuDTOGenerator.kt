package com.littlelemon.application.menu.utils

import com.littlelemon.application.menu.data.remote.models.CategoryDTO
import com.littlelemon.application.menu.data.remote.models.DishDTO
import com.littlelemon.application.menu.data.remote.models.NutritionInfoDTO
import io.github.serpro69.kfaker.faker
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.time.Clock

class MenuDTOGenerator {
    private companion object {
        const val FOUR_YEARS_IN_MILLIS = 4 * 365 * 12 * 30 * 24 * 60 * 60 * 1000L
    }

    fun generateDishDTO(): DishDTO {
        val faker = faker {}
        val nutrition = NutritionInfoDTO(
            calories = (Math.random() * 1000).roundToInt(),
            protein = (Math.random() * 1000).roundToInt(),
            carbs = (Math.random() * 1000).roundToInt(),
            fats = (Math.random() * 1000).roundToInt(),
        )

        val timeNowMillis = Clock.System.now().toEpochMilliseconds()

        return DishDTO(
            id = UUID.randomUUID().toString(),
            title = faker.dessert.dessert()(),
            description = faker.lorem.words(),
            price = (Math.random() * 1000).roundToLong(),
            image = faker.internet.domain(subdomain = true),
            stock = (Math.random() * 1000).roundToInt(),
            nutritionInfo = nutrition,
            discountedPrice = Math.random() * 1000,
            popularityIndex = (0..100).random(),
            dateAdded = (timeNowMillis - Math.random() * FOUR_YEARS_IN_MILLIS).roundToLong(),
            categories = generateCategories((Math.random() * 10).roundToInt()),
        )
    }

    private fun generateCategories(numCategories: Int = 1): List<CategoryDTO> {
        val categories = mutableListOf<CategoryDTO>()
        val faker = faker {}
        repeat(numCategories) {
            categories.add(
                CategoryDTO(
                    categoryName = faker.adjective.positive()
                )
            )
        }
        return categories
    }
}

