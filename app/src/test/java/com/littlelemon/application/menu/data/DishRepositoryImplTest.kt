package com.littlelemon.application.menu.data

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.data.local.dao.DishDao
import com.littlelemon.application.menu.data.remote.MenuRemoteDataSource
import com.littlelemon.application.menu.domain.DishRepository
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import com.littlelemon.application.menu.utils.FakeDishDao
import com.littlelemon.application.menu.utils.FakeDishRemoteDataSource
import com.littlelemon.application.menu.utils.MenuDTOGenerator
import com.littlelemon.application.menu.utils.MenuEntityGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import kotlin.math.roundToInt

class DishRepositoryImplTest() {


    @Nested
    inner class DishSortingTests {

        private lateinit var localDataSource: DishDao
        private lateinit var remoteDataSource: MenuRemoteDataSource
        private lateinit var dishRepository: DishRepository

        @BeforeEach
        fun setUp() {

            localDataSource = FakeDishDao(seedDatabase = true)
            remoteDataSource = FakeDishRemoteDataSource()
            dishRepository = DishRepositoryImpl(localDataSource, remoteDataSource)

        }

        @Test
        fun withNameAscendingSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByNameAscending() =
            runTest {
                // Arrange
                val sorting = DishSorting.NAME_ASCENDING

                // Act
                val result = dishRepository.getDishes(sorting = sorting).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish -> firstDish.title <= secondDish.title }
                    .all { it })
            }

        @Test
        fun withNameDescendingSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByNameDescending() =
            runTest {
                // Arrange
                val sorting = DishSorting.NAME_DESCENDING

                // Act
                val result = dishRepository.getDishes(sorting = sorting).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish -> firstDish.title >= secondDish.title }
                    .all { it })
            }

        @Test
        fun withPriceAscendingSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByPriceAscending() =
            runTest {
                // Arrange
                val sorting = DishSorting.PRICE_ASCENDING

                // Act
                val result = dishRepository.getDishes(sorting = sorting).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish -> firstDish.price <= secondDish.price }
                    .all { it })
            }

        @Test
        fun withPriceDescendingSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByPriceDescending() =
            runTest {
                // Arrange
                val sorting = DishSorting.PRICE_DESCENDING

                // Act
                val result = dishRepository.getDishes(sorting = sorting).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish -> firstDish.price >= secondDish.price }
                    .all { it })
            }

        @Test
        fun withPopularitySortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByPopularityDescending() =
            runTest {
                // Arrange
                val sorting = DishSorting.POPULARITY

                // Act
                val result = dishRepository.getDishes(sorting = sorting).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish -> firstDish.popularityIndex >= secondDish.popularityIndex }
                    .all { it })
            }

        @Test
        fun withLowestCalorieSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByLowestCalories() =
            runTest {
                // Arrange
                val sorting = DishSorting.LOWEST_CALORIES

                // Act
                val result = dishRepository.getDishes(sorting = sorting).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish ->
                    (((firstDish.nutritionInfo?.calories
                        ?: 0) <= (secondDish.nutritionInfo?.calories ?: 0)))
                }
                    .all { it })
            }

        @Test
        fun withHighestCaloriesSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByHighestCalories() =
            runTest {
                // Arrange
                val sorting = DishSorting.HIGHEST_CALORIES

                // Act
                val result = dishRepository.getDishes(sorting = sorting).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish ->
                    (((firstDish.nutritionInfo?.calories
                        ?: 0) >= (secondDish.nutritionInfo?.calories ?: 0)))
                }
                    .all { it })
            }

        @Test
        fun withHighestProteinSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByHighestProtein() =
            runTest {
                // Arrange
                val sorting = DishSorting.HIGHEST_PROTEIN

                // Act
                val result = dishRepository.getDishes(sorting = sorting).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish ->
                    (((firstDish.nutritionInfo?.protein
                        ?: 0) >= (secondDish.nutritionInfo?.protein ?: 0)))
                }
                    .all { it })
            }

        @Test
        fun withRecentlyAddedSortingSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByAddedDateDescending() =
            runTest {
                // Arrange
                val sorting = DishSorting.HIGHEST_PROTEIN

                // Act
                val result = dishRepository.getDishes(sorting = sorting).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish ->
                    firstDish.dateAdded >= secondDish.dateAdded
                }
                    .all { it })
            }

        @Test
        fun withNoSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByPopularity() =
            runTest {
                // Arrange & Act
                val result = dishRepository.getDishes().first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish -> firstDish.popularityIndex >= secondDish.popularityIndex }
                    .all { it })
            }

    }

    @Nested
    inner class FilteringTests {

        private lateinit var localDataSource: DishDao
        private lateinit var remoteDataSource: MenuRemoteDataSource
        private lateinit var dishRepository: DishRepository

        private val outOfStockDishCount: Int = (Math.random() * 10 + 5).roundToInt()

        @BeforeEach
        fun setUp() = runTest {

            localDataSource = FakeDishDao(seedDatabase = true)
            remoteDataSource = FakeDishRemoteDataSource()
            dishRepository = DishRepositoryImpl(localDataSource, remoteDataSource)

            val dishGen = MenuEntityGenerator()
            repeat(outOfStockDishCount) {
                localDataSource.insertDish(dishGen.generateDishEntity().copy(stock = 0))
            }

        }

        @Test
        fun withIncludeOutOfStockFilterApplied_whenGetDishesIsCalled_returnsDishesIncludingOutOfStock() =
            runTest {
                // Arrange
                val filter = DishFilter.INCLUDE_OUT_OF_STOCK

                // Act
                val result = dishRepository.getDishes(filter = filter).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertFalse(result.data.zipWithNext { firstDish, secondDish -> firstDish.stock > 0 }
                    .all { it })

            }

        @Test
        fun withoutIncludeOutOfStockFilterApplied_whenGetDishesIsCalled_returnsDishesThatAreInStock() =
            runTest {
                // Arrange
                val filter = null

                // Act
                val result = dishRepository.getDishes(filter = filter).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertTrue(result.data.zipWithNext { firstDish, secondDish -> firstDish.stock > 0 }
                    .all { it })

            }
    }

    @Nested
    inner class FetchFromRemoteTests {

        private lateinit var localDataSource: DishDao
        private lateinit var remoteDataSource: MenuRemoteDataSource
        private lateinit var dishRepository: DishRepository

        private val entityGenerator = MenuEntityGenerator()
        private val dtoGenerator = MenuDTOGenerator()

        private val ENTITY_TITLE_PREDICATE = "ENTITY: "
        private val DTO_TITLE_PREDICATE = "REMOTE: "

        @BeforeEach
        fun setUp() {

            localDataSource = FakeDishDao(seedDatabase = false)
            remoteDataSource = FakeDishRemoteDataSource(emptyList())
            dishRepository = DishRepositoryImpl(localDataSource, remoteDataSource)

        }

        @Test
        fun withFetchFromRemoteEnabled_whenGetDishesIsCalled_returnsDishesThatAreFetchedFromRemote() =
            runTest {
                // Arrange
                // Populate the data sources
                val numDishEntities = 2
                val dishEntities =
                    List(numDishEntities) { entityGenerator.generateDishEntity() }.map { dish ->
                        dish.copy(
                            title = "$ENTITY_TITLE_PREDICATE: ${dish.title}"
                        )
                    }
                for (entity in dishEntities)
                    localDataSource.insertDish(entity)

                val numDishDTO = 4
                val dishDTO =
                    List(numDishDTO) { dtoGenerator.generateDishDTO() }.map { dish ->
                        dish.copy(
                            title = "$DTO_TITLE_PREDICATE: ${dish.title}"
                        )
                    }
                remoteDataSource = FakeDishRemoteDataSource(dishDTO)

                //Act
                val result = dishRepository.getDishes(fetchFromRemote = true).first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertEquals(numDishDTO, result.data.size)
                assertTrue(result.data.all { dish -> dish.title.startsWith(DTO_TITLE_PREDICATE) })
            }

        @Test
        fun withNoOfflineData_whenGetDishesIsCalled_returnsDishesFetchedFromRemote() = runTest {
            // Arrange
            // Populate the data sources
            val numDishDTO = 5
            val dishDTO =
                List(numDishDTO) { dtoGenerator.generateDishDTO() }.map { dish -> dish.copy(title = "$DTO_TITLE_PREDICATE: ${dish.title}") }
            remoteDataSource = FakeDishRemoteDataSource(dishDTO)

            //Act
            val result = dishRepository.getDishes().first()

            // Assert
            assertTrue(result is Resource.Success)
            result as Resource.Success
            assertNotNull(result.data)
            assertEquals(numDishDTO, result.data.size)
            assertTrue(result.data.all { dish -> dish.title.startsWith(DTO_TITLE_PREDICATE) })
        }

        @Test
        fun withRemoteFailure_whenGetDishesIsCalled_offlineCachedDataIsReturnedWithFetchError() =
            runTest {
                // Arrange
                // Populate the data sources
                val numDishEntities = 3
                val dishEntities =
                    List(numDishEntities) { entityGenerator.generateDishEntity() }.map { dish ->
                        dish.copy(
                            title = "$ENTITY_TITLE_PREDICATE: ${dish.title}"
                        )
                    }
                for (entity in dishEntities)
                    localDataSource.insertDish(entity)

                remoteDataSource = FakeDishRemoteDataSource(throwError = true)

                //Act
                val result = dishRepository.getDishes().first()

                // Assert
                assertTrue(result is Resource.Failure)
                result as Resource.Failure
                assertNotNull(result.data)
                assertEquals(numDishEntities, result.data.size)
                assertTrue(result.data.all { dish -> dish.title.startsWith(ENTITY_TITLE_PREDICATE) })
            }

        @Test
        fun withRemoteFailureAndEmptyOfflineCache_whenGetDishesIsCalled_ReturnsFetchError() =
            runTest {
                // Arrange
                remoteDataSource = FakeDishRemoteDataSource(throwError = true)

                //Act
                val result = dishRepository.getDishes().first()

                // Assert
                assertTrue(result is Resource.Failure)
                result as Resource.Failure
                assertNull(result.data)
                assertNotNull(result.errorMessage)
            }
    }
}