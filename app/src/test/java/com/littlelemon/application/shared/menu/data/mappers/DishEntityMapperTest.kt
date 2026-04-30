package com.littlelemon.application.shared.menu.data.mappers

import com.littlelemon.application.database.menu.models.DishEntity
import com.littlelemon.application.database.menu.models.DishWithCategories
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.menu.utils.DishGenerator
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

class DishEntityMapperTest {

    @Nested
    inner class EntityMapper {
        @Test
        fun givenDishesWithCategories_whenConvertedToDomainObject_returnsCorrectDishesWithCategories() {
            // Given
            val numCategories = 5
            val numDishes = 2
            val dishWithCategoriesAndDateTime =
                DishGenerator.generateDishWithCategories(numDishes, numCategories)

            // Act
            val dishes =
                dishWithCategoriesAndDateTime.map { (dishEntity, _) -> dishEntity.toDish() }

            // Assert
            assertEquals(numDishes, dishes.size)
            assertDishesEqual(dishWithCategoriesAndDateTime, dishes)
        }

        @Test
        fun givenDishesWithCategories_whenConvertedToDomainObject_zeroDiscountedPriceReturnsNullDiscount() {
            // Given
            val numCategories = 3
            val numDishes = 1
            var dishEntity =
                DishGenerator.generateDishWithCategories(numDishes, numCategories).first().first
            dishEntity = dishEntity.copy(dish = dishEntity.dish.copy(discountedPrice = 0.0))

            // Act
            val dish = dishEntity.toDish()

            // Assert
            assertNull(dish.discountedPrice)
        }


        @Test
        fun givenDishesWithEmptyCategories_whenConvertedToEntity_returnsDishesWithoutCategories() {
            // Given
            val numCategories = 0
            val numDishes = 2
            val dishWithCategoriesAndDateTime =
                DishGenerator.generateDishWithCategories(numDishes, numCategories)

            // Act
            val dishes =
                dishWithCategoriesAndDateTime.map { (dishEntity, _) -> dishEntity.toDish() }

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

    @Nested
    inner class CategoryMapper {

        @Test
        fun mapsCategoryToDomainObject() {
            val categoryEntity = DishGenerator.generateCategoryEntities(1).first()
            val category = categoryEntity.toDomainCategory()
            assertEquals(categoryEntity.categoryName, category.categoryName)
        }
    }
}