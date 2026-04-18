package com.littlelemon.application.menu.utils

import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.local.models.DishWithCategories
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.utils.LocalDateTimeGenerator
import io.github.serpro69.kfaker.faker
import kotlinx.datetime.LocalDateTime
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.random.Random


object DishGenerator {
    private val faker = faker {}
    fun generateDishWithCategories(
        numDishes: Int = 2,
        numCategories: Int = 5
    ): List<Pair<DishWithCategories, LocalDateTime>> {
        return List(numDishes) {
            val (dish, dateTime) = generateDishEntity()
            DishWithCategories(
                dish = dish,
                categories = generateCategoryEntities(numCategories)
            ) to dateTime
        }
    }

    fun generateDishEntity(): Pair<DishEntity, LocalDateTime> {
        val nutritionInfo = DishEntity.NutritionInfo(
            calories = (Math.random() * 1000).roundToInt(),
            protein = (Math.random() * 1000).roundToInt(),
            carbs = (Math.random() * 1000).roundToInt(),
            fats = (Math.random() * 1000).roundToInt(),
        )

        val (localDateTime, dateString) = LocalDateTimeGenerator.generateTimestampTZ()
        return DishEntity(
            dishId = UUID.randomUUID().toString(),
            title = faker.dessert.dessert()(),
            description = faker.lorem.words(),
            price = Math.random() * 1000,
            image = faker.internet.domain(subdomain = true),
            stock = (Math.random() * 1000).roundToInt(),
            nutritionInfo = nutritionInfo,
            discountedPrice = Math.random() * 1000,
            popularityIndex = (0..100).random(),
            dateAdded = dateString
        ) to localDateTime
    }

    fun generateCategoryEntities(numCategories: Int = 1): List<CategoryEntity> {
        val categories = mutableListOf<CategoryEntity>()
        repeat(numCategories) {
            categories.add(
                CategoryEntity(
                    categoryId = it.toString(),
                    categoryName = faker.adjective.positive()
                )
            )
        }
        return categories
    }

    // Add Random Number of category associations to dish and create a cross-reference.
    fun generateDishCategoryCrossRef(
        dish: DishEntity,
        categories: List<CategoryEntity>
    ): List<DishCategoryCrossRef> {
        val categoriesToInsert =
            categories.shuffled().drop((Math.random() * categories.size).roundToInt())
        return categoriesToInsert.map { (categoryId, _) ->
            DishCategoryCrossRef(
                dishId = dish.dishId,
                categoryId = categoryId
            )
        }
    }

    fun generateDish(): Dish {
        val nutritionInfo = NutritionInfo(
            calories = (Math.random() * 1000).roundToInt(),
            protein = (Math.random() * 1000).roundToInt(),
            carbs = (Math.random() * 1000).roundToInt(),
            fats = (Math.random() * 1000).roundToInt(),
        )
        return Dish(
            title = faker.dessert.dessert()(),
            description = faker.lorem.toString(),
            price = Math.random() * 1000,
            imageURL = faker.internet.safeDomainSuffix() + "/" + faker.dessert.dessert()() + ".png",
            stock = (Math.random() * 1000).roundToInt(),
            nutritionInfo = nutritionInfo,
            discountedPrice = Math.random() * 1000,
            popularityIndex = (0..100).random(),
            dateAdded = LocalDateTime(2024, 5, 5, 10, 12, 0),
            category = List(Random.nextInt(3)) { Category(faker.adjective.toString()) }
        )
    }

}
