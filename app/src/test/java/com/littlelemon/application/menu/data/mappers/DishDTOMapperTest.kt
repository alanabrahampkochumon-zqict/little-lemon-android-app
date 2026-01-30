package com.littlelemon.application.menu.data.mappers

import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.remote.models.CategoryDTO
import com.littlelemon.application.menu.data.remote.models.DishDTO
import com.littlelemon.application.menu.data.remote.models.NutritionInfoDTO
import com.littlelemon.application.menu.utils.MenuDTOGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class DishDTOMapperTest {

    @Test
    fun givenDishesWithCategories_whenConvertedToEntity_returnsCorrectDishesWithCategories() {
        // Given
        val numCategories = 5
        val numDishes = 2
        val dtoGenerator = MenuDTOGenerator()
        val dishDTOs = List(numDishes) { dtoGenerator.generateDishDTO(numCategories) }

        // Act
        val (dishes, categories, crossRef) = dishDTOs.toDishWithCategories()

        // Assert
        assertEquals(numDishes, dishes.size)
        assertEquals(numCategories * numDishes, categories.size)
        assertEquals(numCategories * numDishes, crossRef.size)

        assertDishesEqual(dishDTOs, dishes)
        assertTrue(dishDTOs.map { dish ->
            dish.categories.all { categoryDTO ->
                categories.find { categoryEntity ->
                    categoryDTO.categoryId == categoryEntity.categoryId && categoryDTO.categoryName == categoryEntity.categoryName
                } != null
            }
        }.all { matches -> matches })
        assertCrossRefExists(dishDTOs, crossRef)
    }


    @Test
    fun givenDishesWithEmptyCategories_whenConvertedToEntity_returnsDishesWithoutCategories() {
        // Given
        val numCategories = 0
        val numDishes = 2
        val dtoGenerator = MenuDTOGenerator()
        val dishDTOs = List(numDishes) { dtoGenerator.generateDishDTO(numCategories) }

        // Act
        val (dishes, categories, crossRef) = dishDTOs.toDishWithCategories()

        // Assert
        assertEquals(numDishes, dishes.size)
        assertEquals(numCategories, categories.size)
        assertEquals(numCategories, crossRef.size)

        assertDishesEqual(dishDTOs, dishes)
    }

    @Test
    fun givenDishesDuplicateCategories_whenConvertedToEntity_returnsDishesWithoutDuplicatesCategories() {
        // Given
        val categoriesDTO1 =
            listOf(CategoryDTO("1", "category1"), CategoryDTO("1", "category1"))
        val categoriesDTO2 =
            listOf(CategoryDTO("2", "category2"), CategoryDTO("1", "category1"))
        val numDishes = 2
        val dtoGenerator = MenuDTOGenerator()
        val dishDTOs = MutableList(numDishes) { dtoGenerator.generateDishDTO(0) }
        dishDTOs[0] = dishDTOs[0].copy(categories = categoriesDTO1)
        dishDTOs[1] = dishDTOs[1].copy(categories = categoriesDTO2)

        // Act
        val (dishes, categories, crossRef) = dishDTOs.toDishWithCategories()

        // Assert
        assertEquals(numDishes, dishes.size)
        assertEquals(2, categories.size)
        assertEquals(2, crossRef.size)

        assertTrue(categories.find { (_, categoryName) -> categoryName == "category1" } != null)
        assertTrue(categories.find { (_, categoryName) -> categoryName == "category2" } != null)
        assertTrue(crossRef.find { (_, categoryId) -> categoryId == "1" } != null)
        assertTrue(crossRef.find { (_, categoryId) -> categoryId == "2" } != null)
    }

    private fun assertCrossRefExists(
        dishDTOs: List<DishDTO>,
        crossRef: List<DishCategoryCrossRef>
    ) {
        dishDTOs.forEach { dishDTO ->
            dishDTO.categories.forEach { (categoryDTOId) ->
                assertNotNull(crossRef.find { (dishId, categoryId) -> dishId == dishDTO.id && categoryId == categoryDTOId })
            }
        }
    }

    private fun assertDishesEqual(
        dishDTOs: List<DishDTO>,
        dishes: List<DishEntity>
    ) {

        dishDTOs.forEachIndexed { index, dishDTO ->
            assertEquals(dishDTO.title, dishes[index].title)
            assertEquals(dishDTO.description, dishes[index].description)
            assertEquals(dishDTO.price / 100.0, dishes[index].price)
            assertEquals(dishDTO.image, dishes[index].image)
            assertEquals(dishDTO.stock, dishes[index].stock)
            assertEquals(dishDTO.discountedPrice / 100.0, dishes[index].discountedPrice)
            assertEquals(dishDTO.popularityIndex, dishes[index].popularityIndex)
            assertEquals(dishDTO.dateAdded, dishes[index].dateAdded)

            assertNutritionInfoEquals(dishDTO.nutritionInfo, dishes[index].nutritionInfo)
        }

    }

    private fun assertNutritionInfoEquals(
        nutritionInfoDTO: NutritionInfoDTO?,
        nutritionInfoEntity: DishEntity.NutritionInfo?
    ) {
        nutritionInfoDTO?.let { nutritionInfoDTO ->
            // If NutritionInfoDTO is not null, then  the corresponding entity must not also be null.
            assertNotNull(nutritionInfoEntity)
            assertEquals(nutritionInfoDTO.calories, nutritionInfoEntity.calories)
            assertEquals(nutritionInfoDTO.carbs, nutritionInfoEntity.carbs)
            assertEquals(nutritionInfoDTO.fats, nutritionInfoEntity.fats)
            assertEquals(nutritionInfoDTO.protein, nutritionInfoEntity.protein)
        }
    }


}