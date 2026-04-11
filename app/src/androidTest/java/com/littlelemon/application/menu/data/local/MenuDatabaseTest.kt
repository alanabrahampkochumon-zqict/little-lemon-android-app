package com.littlelemon.application.menu.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.littlelemon.application.menu.data.local.dao.MenuDao
import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.utils.DishEntityGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class MenuDatabaseTest {

    private lateinit var database: MenuDatabase
    private lateinit var dao: MenuDao


    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context = context, klass = MenuDatabase::class.java)
            .build()
        dao = database.dao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun dishWithNoCategoriesInserted_whenQueried_returnsCorrectDish() = runTest {
        // Arrange
        val numCategories = 0
        val dish = DishEntityGenerator.generateDishEntity().first
        val categories = DishEntityGenerator.generateCategoryEntities(numCategories)
        val crossRef = categories.map { (categoryId, _) ->
            DishCategoryCrossRef(
                dishId = dish.dishId,
                categoryId = categoryId
            )
        }
        dao.insertDishes(listOf(dish), categories, crossRef)

        // Act
        val result = dao.getAllDishes().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(dish, result.first().dish)
        assertEquals(numCategories, result.first().categories.size)
    }

    @Test
    fun dishWithOneCategoriesInserted_whenQueried_returnsCorrectDishWithOneCategory() = runTest {
        // Arrange
        val numCategories = 1
        val dish = DishEntityGenerator.generateDishEntity().first
        val categories = DishEntityGenerator.generateCategoryEntities(numCategories)
        val crossRef = categories.map { (categoryId, _) ->
            DishCategoryCrossRef(
                dishId = dish.dishId,
                categoryId = categoryId
            )
        }
        dao.insertDishes(listOf(dish), categories, crossRef)

        // Act
        val result = dao.getAllDishes().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(dish, result.first().dish.copy())
        assertEquals(numCategories, result.first().categories.size)
        assertEquals(categories.first(), result.first().categories[0])
    }

    @Test
    fun dishWithTwoCategoriesInserted_whenQueried_returnsCorrectDishWithTwoCategories() = runTest {
        // Arrange
        val numCategories = 2
        val dish = DishEntityGenerator.generateDishEntity().first
        val categories = DishEntityGenerator.generateCategoryEntities(numCategories)
        val crossRef = categories.map { (categoryId, _) ->
            DishCategoryCrossRef(
                dishId = dish.dishId,
                categoryId = categoryId
            )
        }
        dao.insertDishes(listOf(dish), categories, crossRef)

        // Act
        val result = dao.getAllDishes().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(dish, result.first().dish.copy())
        assertEquals(numCategories, result.first().categories.size)

        for (cat in categories) {
            assertTrue(cat in result.first().categories)
        }
    }

    @Test
    fun emptyDatabase_whenQueriedForDishCount_returnsZero() = runTest {
        // Arrange & Act
        val result = dao.getDishCount()

        // Assert
        assertEquals(0, result)
    }


    @Test
    fun nonEmptyDatabase_whenQueriedForDishCount_returnsCorrectItemCount() = runTest {
        // Arrange
        val numDishes = 5
        insertDishes(numDishes)

        // Act
        val result = dao.getDishCount()

        // Assert
        assertEquals(numDishes, result)
    }

    @Test
    fun dishesDeletedFromDBWithOnlyOneDish_whenQueried_returnsEmptyList() = runTest {
        // Arrange
        val numDishes = 1
        insertDishes(numDishes)

        // Act
        dao.deleteAllDishes()
        val result = dao.getAllDishes().first()

        // Assert
        assertEquals(0, result.size)
    }

    @Test
    fun dishesDeletedFromDBWithMultipleDish_whenQueried_returnsListWithoutDeletedDish() = runTest {
        // Arrange
        val numDishes = 7
        insertDishes(numDishes)

        // Act
        dao.deleteAllDishes()
        val result = dao.getAllDishes().first()

        // Assert
        assertEquals(0, result.size)

    }

    @Test
    fun dishesSortedByNameAscending_whenQueried_returnsListOfDishesSortedByNameAscending() =
        runTest {
            // Arrange
            val numDishes = 7
            insertDishes(numDishes)

            // Act
            val result = dao.getDishesSortedByName(ascending = true).first()

            // Assert
            assertEquals(numDishes, result.size)
            assertTrue(result.zipWithNext { firstDish, secondDish -> firstDish.dish.title <= secondDish.dish.title }
                .all { it })
        }

    @Test
    fun dishesSortedByNameDescending_whenQueried_returnsListOfDishesSortedByNameDescending() =
        runTest {
            // Arrange
            val numDishes = 7
            insertDishes(numDishes)

            // Act
            val result = dao.getDishesSortedByName(ascending = false).first()

            // Assert
            assertEquals(numDishes, result.size)
            assertTrue(result.zipWithNext { firstDish, secondDish -> firstDish.dish.title >= secondDish.dish.title }
                .all { it })
        }

    @Test
    fun dishesSortedByPriceAscending_whenQueried_returnsListOfDishesSortedByPriceAscending() =
        runTest {
            // Arrange
            val numDishes = 7
            insertDishes(numDishes)

            // Act
            val result = dao.getDishesSortedByPrice(ascending = true).first()

            // Assert
            assertEquals(numDishes, result.size)
            assertTrue(result.zipWithNext { firstDish, secondDish -> firstDish.dish.price <= secondDish.dish.price }
                .all { it })
        }

    @Test
    fun dishesSortedByPriceDescending_whenQueried_returnsListOfDishesSortedByPriceDescending() =
        runTest {
            // Arrange
            val numDishes = 7
            insertDishes(numDishes)

            // Act
            val result = dao.getDishesSortedByPrice(ascending = false).first()

            // Assert
            assertEquals(numDishes, result.size)
            assertTrue(result.zipWithNext { firstDish, secondDish -> firstDish.dish.price >= secondDish.dish.price }
                .all { it })
        }


    @Test
    fun dishesSortedByPopularity_whenQueried_returnsListOfDishesSortedByPopularity() = runTest {
        // Arrange
        val numDishes = 7
        insertDishes(numDishes)

        // Act
        val result = dao.getDishesSortedByPopularity().first()

        // Assert
        assertEquals(numDishes, result.size)
        assertTrue(result.zipWithNext { firstDish, secondDish -> firstDish.dish.popularityIndex >= secondDish.dish.popularityIndex }
            .all { it })
    }

    @Test
    fun dishesSortedByRecently_whenQueried_returnsListOfDishesSortedByDateDescending() = runTest {
        // Arrange
        val numDishes = 7
        insertDishes(numDishes)

        // Act
        val result = dao.getDishesSortedByAdded(ascending = false).first()

        // Assert
        assertEquals(numDishes, result.size)
        assertTrue(result.zipWithNext { firstDish, secondDish -> firstDish.dish.dateAdded >= secondDish.dish.dateAdded }
            .all { it })
    }

    @Test
    fun dishesSortedByCaloriesAscending_whenQueried_returnsListOfDishesSortedByCaloriesAscending() =
        runTest {
            // Arrange
            val numDishes = 7
            insertDishes(numDishes)

            // Act
            val result = dao.getDishesSortedByCalories().first()

            // Assert
            assertEquals(numDishes, result.size)
            assertTrue(result.zipWithNext { firstDish, secondDish ->
                (firstDish.dish.nutritionInfo?.calories
                    ?: 0) <= (secondDish.dish.nutritionInfo?.calories ?: 0)
            }
                .all { it })
        }

    @Test
    fun dishesSortedByCaloriesDescending_whenQueried_returnsListOfDishesSortedByCaloriesDescending() =
        runTest {
            // Arrange
            val numDishes = 7
            insertDishes(numDishes)

            // Act
            val result = dao.getDishesSortedByCalories(ascending = false).first()

            // Assert
            assertEquals(numDishes, result.size)
            assertTrue(result.zipWithNext { firstDish, secondDish ->
                (firstDish.dish.nutritionInfo?.calories
                    ?: 0) >= (secondDish.dish.nutritionInfo?.calories ?: 0)
            }
                .all { it })
        }

    @Test
    fun dishesSortedByProteinAscending_whenQueried_returnsListOfDishesSortedByProteinAscending() =
        runTest {
            // Arrange
            val numDishes = 7
            insertDishes(numDishes)

            // Act
            val result = dao.getDishesSortedByProtein().first()

            // Assert
            assertEquals(numDishes, result.size)
            assertTrue(result.zipWithNext { firstDish, secondDish ->
                (firstDish.dish.nutritionInfo?.protein
                    ?: 0) <= (secondDish.dish.nutritionInfo?.protein ?: 0)
            }
                .all { it })
        }

    @Test
    fun dishesSortedByProteinDescending_whenQueried_returnsListOfDishesSortedByProteinDescending() =
        runTest {
            // Arrange
            val numDishes = 7
            insertDishes(numDishes)

            // Act
            val result = dao.getDishesSortedByProtein(ascending = false).first()

            // Assert
            assertEquals(numDishes, result.size)
            assertTrue(result.zipWithNext { firstDish, secondDish ->
                (firstDish.dish.nutritionInfo?.protein
                    ?: 0) >= (secondDish.dish.nutritionInfo?.protein ?: 0)
            }
                .all { it })
        }

    private suspend fun insertDishes(numDishes: Int = 7) {
        val dishes = List(numDishes) { DishEntityGenerator.generateDishEntity().first }
        val categories = mutableListOf<CategoryEntity>()
        val crossRefs = mutableListOf<DishCategoryCrossRef>()
        dishes.forEach { dish ->
            categories.addAll(DishEntityGenerator.generateCategoryEntities(Random.nextInt(5, 20)))
            crossRefs.addAll(categories.map { (categoryId, _) ->
                DishCategoryCrossRef(
                    dishId = dish.dishId,
                    categoryId = categoryId
                )
            })
        }
        dao.insertDishes(dishes, categories, crossRefs)
    }

}