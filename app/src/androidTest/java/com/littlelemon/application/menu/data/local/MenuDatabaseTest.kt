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
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class MenuDatabaseTest {

    private lateinit var database: MenuDatabase
    private lateinit var dao: DishWithCategoriesDao

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

    private fun generateDish(): DishEntity {
        val faker = faker {}
        val nutrition = DishEntity.Nutrition(
            calories = (Math.random() * 1000).roundToInt(),
            protein = (Math.random() * 1000).roundToInt(),
            carbs = (Math.random() * 1000).roundToInt(),
            fats = (Math.random() * 1000).roundToInt(),
        )

        return DishEntity(
            title = faker.dessert.dessert()(),
            description = faker.lorem.words(),
            price = Math.random() * 1000,
            image = faker.internet.domain(subdomain = true),
            stock = (Math.random() * 1000).roundToInt(),
            nutritionInfo = nutrition,
            discountedPrice = Math.random() * 1000,
            popularityIndex = (0..100).random()
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