package com.littlelemon.application.menu.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.littlelemon.application.menu.data.local.dao.DishWithCategoriesDao
import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.local.models.DishWithCategories
import io.github.serpro69.kfaker.faker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

@RunWith(AndroidJUnit4::class)
class MenuDatabaseTest {

    private lateinit var database: MenuDatabase
    private lateinit var dao: DishWithCategoriesDao

    private val FOUR_YEARS_IN_MILLIS = 4 * 365 * 12 * 30 * 24 * 60 * 60 * 1000L

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
        val dish = DishWithCategories(
            dish = generateDish(),
            categories = emptyList()
        )

        // Act
        dao.insertDish(dish)
        val result = dao.getAllDishes().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(dish.dish, result.first().dish.copy(dishId = null))
        assertEquals(0, result.first().categories.size)
    }

    @Test
    fun dishWithOneCategoriesInserted_whenQueried_returnsCorrectDishWithTwoCategories() = runTest {
        // Arrange
        val dish = DishWithCategories(
            dish = generateDish(),
            categories = generateCategories(1)
        )

        // Act
        dao.insertDish(dish)
        val result = dao.getAllDishes().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(dish.dish, result.first().dish.copy(dishId = null))
        assertEquals(1, result.first().categories.size)
        assertTrue(result.first().categories.any { (_, actualCategoryName) -> actualCategoryName == dish.categories.first().categoryName })
    }

    @Test
    fun dishWithTwoCategoriesInserted_whenQueried_returnsCorrectDishWithTwoCategories() = runTest {
        // Arrange
        val dish = DishWithCategories(
            dish = generateDish(),
            categories = generateCategories(2)
        )

        // Act
        dao.insertDish(dish)
        val result = dao.getAllDishes().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(dish.dish, result.first().dish.copy(dishId = null))
        assertEquals(2, result.first().categories.size)

        for ((_, categoryName) in dish.categories) {
            assertTrue(result.first().categories.any { (_, actualCategoryName) -> actualCategoryName == categoryName })
        }
    }

    @Test
    fun dishesDeletedFromDBWithOnlyOneDish_whenQueried_returnsEmptyList() = runTest {
        // Arrange
        val dish = DishWithCategories(
            dish = generateDish(),
            categories = generateCategories(2)
        )
        dao.insertDish(dish)

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
        repeat(numDishes) {

            val dish = DishWithCategories(
                dish = generateDish(),
                categories = generateCategories(it)
            )
            dao.insertDish(dish)
        }

        // Act
        dao.deleteAllDishes()
        val result = dao.getAllDishes().first()

        // Assert
        assertEquals(0, result.size)

    }

    @Test
    fun dishesSortedByPopularity_whenQueried_returnsListOfDishesSortedByPopularity() = runTest {
        // Arrange
        val numDishes = 7
        repeat(numDishes) {
            val dish = DishWithCategories(
                dish = generateDish(),
                categories = generateCategories(it)
            )
            dao.insertDish(dish)
        }

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
        repeat(numDishes) {
            val dish = DishWithCategories(
                dish = generateDish(),
                categories = generateCategories(it)
            )
            dao.insertDish(dish)
        }

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
            repeat(numDishes) {
                val dish = DishWithCategories(
                    dish = generateDish(),
                    categories = generateCategories(it)
                )
                dao.insertDish(dish)
            }

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
            repeat(numDishes) {
                val dish = DishWithCategories(
                    dish = generateDish(),
                    categories = generateCategories(it)
                )
                dao.insertDish(dish)
            }

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
            repeat(numDishes) {
                val dish = DishWithCategories(
                    dish = generateDish(),
                    categories = generateCategories(it)
                )
                dao.insertDish(dish)
            }

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
            repeat(numDishes) {
                val dish = DishWithCategories(
                    dish = generateDish(),
                    categories = generateCategories(it)
                )
                dao.insertDish(dish)
            }

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


    private fun generateDish(popularityIndex: Int? = null): DishEntity {
        val faker = faker {}
        val nutrition = DishEntity.Nutrition(
            calories = (Math.random() * 1000).roundToInt(),
            protein = (Math.random() * 1000).roundToInt(),
            carbs = (Math.random() * 1000).roundToInt(),
            fats = (Math.random() * 1000).roundToInt(),
        )

        val timeNowMillis = Clock.System.now().toEpochMilliseconds()

        return DishEntity(
            title = faker.dessert.dessert()(),
            description = faker.lorem.words(),
            price = Math.random() * 1000,
            image = faker.internet.domain(subdomain = true),
            stock = (Math.random() * 1000).roundToInt(),
            nutritionInfo = nutrition,
            discountedPrice = Math.random() * 1000,
            popularityIndex = popularityIndex ?: (0..100).random(),
            dateAdded = (timeNowMillis - Math.random() * FOUR_YEARS_IN_MILLIS).roundToLong() // TODO: Update
        )
    }

    private fun generateCategories(numCategories: Int = 1): List<CategoryEntity> {
        val categories = mutableListOf<CategoryEntity>()
        val faker = faker {}
        repeat(numCategories) {
            categories.add(
                CategoryEntity(
                    categoryName = faker.adjective.positive()
                )
            )
        }
        return categories
    }


}