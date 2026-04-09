package com.littlelemon.application.menu.utils

import com.littlelemon.application.menu.data.remote.models.CategoryDTO
import com.littlelemon.application.menu.data.remote.models.DishDTO
import com.littlelemon.application.menu.data.remote.models.NutritionInfoDTO
import io.github.serpro69.kfaker.faker
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant

class MenuDTOGenerator {
    private companion object {
        const val FOUR_YEARS_IN_MILLIS = 4 * 365 * 12 * 30 * 24 * 60 * 60 * 1000L
    }

    fun generateDishDTO(numCategories: Int = 5): Pair<DishDTO, LocalDateTime> {
        val faker = faker {}
        val nutrition = NutritionInfoDTO(
            calories = (Math.random() * 1000).roundToInt(),
            protein = (Math.random() * 1000).roundToInt(),
            carbs = (Math.random() * 1000).roundToInt(),
            fats = (Math.random() * 1000).roundToInt(),
        )

        val randomSeconds = Random.nextLong(365L * 24 * 60 * 60)
        val instant = Instant.fromEpochMilliseconds(
            Clock.System.now().toEpochMilliseconds() - randomSeconds * 1000L
        )
        val localTimeZone = TimeZone.currentSystemDefault()
        val localDateTime = instant.toLocalDateTime(localTimeZone)
        val offset = localTimeZone.offsetAt(instant)

        return DishDTO(
            id = UUID.randomUUID().toString(),
            title = faker.dessert.dessert()(),
            description = faker.lorem.words(),
            price = (Math.random() * 1000).roundToLong(),
            image = faker.internet.domain(subdomain = true),
            stock = (Math.random() * 1000).roundToInt(),
            nutritionInfo = nutrition,
            discountedPrice = (Math.random() * 10000).roundToLong(),
            popularityIndex = (0..100).random(),
            dateAdded = "$localDateTime$offset",
            categories = generateCategories(numCategories),
        ) to localDateTime
    }

    private fun generateCategories(numCategories: Int = 1): List<CategoryDTO> {
        val categories = mutableListOf<CategoryDTO>()
        val faker = faker {}
        repeat(numCategories) {
            categories.add(
                CategoryDTO(
                    categoryId = UUID.randomUUID().toString(),
                    categoryName = faker.adjective.positive()
                )
            )
        }
        return categories
    }
}

