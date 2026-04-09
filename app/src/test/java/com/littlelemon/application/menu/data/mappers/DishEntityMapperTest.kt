package com.littlelemon.application.menu.data.mappers

import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.local.models.DishWithCategories
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.menu.utils.DishEntityGenerator
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class DishEntityMapperTest {

    @Test
    fun givenDishesWithCategories_whenConvertedToDomainObject_returnsCorrectDishesWithCategories() {
        // Given
        val numCategories = 5
        val numDishes = 2
        val dishWithCategoriesAndDateTime =
            DishEntityGenerator.generateDishWithCategories(numDishes, numCategories)

        // Act
        val dishes = dishWithCategoriesAndDateTime.map { (dishEntity, _) -> dishEntity.toDish() }

        // Assert
        assertEquals(numDishes, dishes.size)
        assertDishesEqual(dishWithCategoriesAndDateTime, dishes)
    }


    @Test
    fun givenDishesWithEmptyCategories_whenConvertedToEntity_returnsDishesWithoutCategories() {
        // Given
        val numCategories = 0
        val numDishes = 2
        val dishWithCategoriesAndDateTime =
            DishEntityGenerator.generateDishWithCategories(numDishes, numCategories)

        // Act
        val dishes = dishWithCategoriesAndDateTime.map { (dishEntity, _) -> dishEntity.toDish() }

        // Assert
        assertEquals(numDishes, dishes.size)
        assertDishesEqual(dishWithCategoriesAndDateTime, dishes)
    }

    private fun assertDishesEqual(
        dishWithCategories: List<Pair<DishWithCategories, LocalDateTime>>,
        dishes: List<Dish>
    ) {

        dishWithCategories.forEachIndexed { index, (dishWithCat, dateTime) ->
            val (dish, category) = dishWithCat
            assertEquals(dish.title, dishes[index].title)
            assertEquals(dish.description, dishes[index].description)
            assertEquals(dish.price, dishes[index].price)
            assertEquals(dish.image, dishes[index].imageURL)
            assertEquals(dish.stock, dishes[index].stock)
            assertEquals(dish.discountedPrice, dishes[index].discountedPrice)
            assertEquals(dish.popularityIndex, dishes[index].popularityIndex)
            assertEquals(dateTime, dishes[index].dateAdded)

            assertNutritionInfoEquals(dish.nutritionInfo, dishes[index].nutritionInfo)

            assertEquals(category.size, dishes[index].category.size)
            assertTrue(category.all { (_, categoryName) -> dishes[index].category.find { dishCategory -> categoryName == dishCategory.categoryName } != null })
        }

    }

    private fun assertNutritionInfoEquals(
        nutritionInfoEntity: DishEntity.NutritionInfo?,
        nutritionInfo: NutritionInfo?
    ) {
        nutritionInfoEntity?.let { nutritionInfoEntity ->
            // If NutritionInfoDTO is not null, then  the corresponding entity must not also be null.
            assertNotNull(nutritionInfo)
            assertEquals(nutritionInfoEntity.calories, nutritionInfo.calories)
            assertEquals(nutritionInfoEntity.carbs, nutritionInfo.carbs)
            assertEquals(nutritionInfoEntity.fats, nutritionInfo.fats)
            assertEquals(nutritionInfoEntity.protein, nutritionInfo.protein)
        }
    }
}