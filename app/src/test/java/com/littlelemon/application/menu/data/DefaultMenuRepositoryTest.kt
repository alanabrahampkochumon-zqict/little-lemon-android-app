package com.littlelemon.application.menu.data

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.data.local.dao.MenuDao
import com.littlelemon.application.menu.data.mappers.toDish
import com.littlelemon.application.menu.data.remote.MenuRemoteDataSource
import com.littlelemon.application.menu.domain.MenuRepository
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.menu.utils.FakeDishRemoteDataSource
import com.littlelemon.application.menu.utils.FakeMenuDao
import com.littlelemon.application.menu.utils.MenuDTOGenerator
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
import kotlin.test.assertContains
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DefaultMenuRepositoryTest {

    private lateinit var localDataSource: MenuDao
    private lateinit var remoteDataSource: MenuRemoteDataSource
    private lateinit var menuRepository: MenuRepository

    @BeforeEach
    fun setUp() {
        localDataSource = FakeMenuDao(seedDatabase = true)
        remoteDataSource = FakeDishRemoteDataSource()
        menuRepository = DefaultMenuRepository(localDataSource, remoteDataSource)

    }

    @Nested
    inner class DishSortingTests {

        @Test
        fun withNameAscendingSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByNameAscending() =
            runTest {
                // Arrange
                val sorting = DishSorting.NAME_ASCENDING

                // Act
                val result = menuRepository.getDishes(sorting = sorting)
                    .filter { res -> res is Resource.Success }.first()
                print(result)
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
                val result = menuRepository.getDishes(sorting = sorting)
                    .filter { res -> res is Resource.Success }.first()

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
                val result = menuRepository.getDishes(sorting = sorting)
                    .filter { res -> res is Resource.Success }.first()

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
                val result = menuRepository.getDishes(sorting = sorting)
                    .filter { res -> res is Resource.Success }.first()

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
                val result = menuRepository.getDishes(sorting = sorting)
                    .filter { res -> res is Resource.Success }.first()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                print(result.data.map { it.popularityIndex })
                assertTrue(result.data.zipWithNext { firstDish, secondDish -> firstDish.popularityIndex >= secondDish.popularityIndex }
                    .all { it })
            }

        @Test
        fun withLowestCalorieSortingApplied_whenGetDishesIsCalled_returnsDishesOrderedByLowestCalories() =
            runTest {
                // Arrange
                val sorting = DishSorting.LOWEST_CALORIES

                // Act
                val result = menuRepository.getDishes(sorting = sorting)
                    .filter { res -> res is Resource.Success }.first()

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
                val result = menuRepository.getDishes(sorting = sorting)
                    .filter { res -> res is Resource.Success }.first()

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
                val result = menuRepository.getDishes(sorting = sorting)
                    .filter { res -> res is Resource.Success }.first()

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
                val sorting = DishSorting.RECENTLY_ADDED

                // Act
                val result = menuRepository.getDishes(sorting = sorting)
                    .filter { res -> res is Resource.Success }.first()

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
                val result =
                    menuRepository.getDishes().filter { res -> res is Resource.Success }.first()

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

        private val outOfStockDishCount: Int = (Math.random() * 10 + 5).roundToInt()

        @BeforeEach
        fun setUp() = runTest {
            localDataSource.insertDishes(List(outOfStockDishCount) {
                DishGenerator.generateDishEntity().first.copy(stock = 0)
            })
        }

        @Test
        fun withIncludeOutOfStockFilterApplied_whenGetDishesIsCalled_returnsDishesIncludingOutOfStock() =
            runTest {
                // Arrange
                val filter = DishFilter.INCLUDE_OUT_OF_STOCK

                // Act
                val result = menuRepository.getDishes(filter = filter)
                    .filter { res -> res is Resource.Success }.first()

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
                val result = menuRepository.getDishes(filter = filter)
                    .filter { res -> res is Resource.Success }.first()

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

        private val entityGenerator = DishGenerator
        private val dtoGenerator = MenuDTOGenerator()

        private val ENTITY_TITLE_PREDICATE = "ENTITY: "
        private val DTO_TITLE_PREDICATE = "REMOTE: "

        @BeforeEach
        fun setUp() {
            localDataSource = FakeMenuDao(false)
        }

        @Test
        fun withFetchFromRemoteEnabled_whenGetDishesIsCalled_returnsDishesThatAreFetchedFromRemote() =
            runTest {
                // Arrange
                // Populate the data sources
                val numDishEntities = 2
                val dishEntities =
                    List(numDishEntities) { entityGenerator.generateDishEntity().first }.map { dish ->
                        dish.copy(
                            title = "$ENTITY_TITLE_PREDICATE: ${dish.title}"
                        )
                    }
                localDataSource.insertDishes(dishEntities)

                val numDishDTO = 4
                val dishDTO =
                    List(numDishDTO) { dtoGenerator.generateDishDTO().first }.map { dish ->
                        dish.copy(
                            title = "$DTO_TITLE_PREDICATE: ${dish.title}"
                        )
                    }
                remoteDataSource = FakeDishRemoteDataSource(dishDTO)
                menuRepository = DefaultMenuRepository(localDataSource, remoteDataSource)

                //Act
                val result = menuRepository.getDishes(fetchFromRemote = true)
                    .filter { res -> res is Resource.Success }.first()
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
                List(numDishDTO) { dtoGenerator.generateDishDTO().first }.map { dish ->
                    dish.copy(
                        title = "$DTO_TITLE_PREDICATE: ${dish.title}"
                    )
                }
            remoteDataSource = FakeDishRemoteDataSource(dishDTO)
            menuRepository = DefaultMenuRepository(localDataSource, remoteDataSource)

            //Act
            val result =
                menuRepository.getDishes().filter { res -> res is Resource.Success }.first()

            // Assert
            assertTrue(result is Resource.Success)
            result as Resource.Success
            assertNotNull(result.data)
            assertEquals(numDishDTO, result.data.size)
            assertTrue(result.data.all { dish -> dish.title.startsWith(DTO_TITLE_PREDICATE) })
        }

        @Test
        fun withRemoteFailure_whenGetDishesIsCalled_offlineCachedDataIsReturned() =
            runTest {
                // Arrange
                // Populate the data sources
                val numDishEntities = 3
                val dishEntities =
                    List(numDishEntities) { entityGenerator.generateDishEntity().first }.map { dish ->
                        dish.copy(
                            title = "$ENTITY_TITLE_PREDICATE: ${dish.title}"
                        )
                    }

                localDataSource.insertDishes(dishEntities)
                remoteDataSource = FakeDishRemoteDataSource(throwError = true)
                menuRepository = DefaultMenuRepository(localDataSource, remoteDataSource)

                //Act
                val result =
                    menuRepository.getDishes().take(2).toList().last()

                // Assert
                assertTrue(result is Resource.Success)
                result as Resource.Success
                assertNotNull(result.data)
                assertEquals(numDishEntities, result.data.size)
                assertTrue(result.data.all { dish -> dish.title.startsWith(ENTITY_TITLE_PREDICATE) })
            }

        @Test
        fun withRemoteFailureAndEmptyOfflineCache_whenGetDishesIsCalled_ReturnsFetchError() =
            runTest {
                // Arrange
                remoteDataSource = FakeDishRemoteDataSource(throwError = true)
                menuRepository = DefaultMenuRepository(localDataSource, remoteDataSource)


                //Act
                val result =
                    menuRepository.getDishes().filter { res -> res is Resource.Failure }.first()

                // Assert
                assertTrue(result is Resource.Failure)
                result as Resource.Failure
                assertNull(result.data)
            }
    }

    @Nested
    inner class FilterCategoryTesting {


        @Test
        fun nullFilterCategory_returnsAllDishes() = runTest {
            // Given null filterCategory.
            val filterCategory: String? = null
            val expectedResult = localDataSource.getAllDishes().first().map { it.toDish() }

            // When getDishes is invoked
            val result = menuRepository.getDishes(filterCategory = filterCategory)
                .filter { res -> res is Resource.Success }.first()

            // Then, the result is success with all the dishes.
            assertTrue(result is Resource.Success)
            result as Resource.Success
            assertNotNull(result.data)
            expectedResult.forEach { dish ->
                assertContains(result.data, dish)
            }
        }

        @Test
        fun blankFilterCategory_returnsAllDishes() = runTest {
            // Given null filterCategory.
            val filterCategory = ""
            val expectedResult = localDataSource.getAllDishes().first().map { it.toDish() }

            // When getDishes is invoked
            val result = menuRepository.getDishes(filterCategory = filterCategory)
                .filter { res -> res is Resource.Success }.first()

            // Then, the result is success with all the dishes.
            assertTrue(result is Resource.Success)
            result as Resource.Success
            assertNotNull(result.data)
            expectedResult.forEach { dish ->
                assertContains(result.data, dish)
            }
        }

        @Test
        fun validFilterCategory_returnsDishesWithThatCategory() = runTest {
            // Given valid filter category.
            val filterCategory = (localDataSource as FakeMenuDao).getAllCategories().first().first()
            val expectedResult =
                localDataSource.getAllDishes().first().filter { filterCategory in it.categories }
                    .map { it.toDish() }

            // When getDishes is invoked
            val result = menuRepository.getDishes(filterCategory = filterCategory.categoryName)
                .filter { res -> res is Resource.Success }.first()

            // Then, the result is success with only dishes having that category.
            assertTrue(result is Resource.Success)
            result as Resource.Success
            assertNotNull(result.data)
            expectedResult.forEach { dish ->
                assertContains(result.data, dish)
            }
        }


        @OptIn(ExperimentalUuidApi::class)
        @Test
        fun invalidFilterCategory_returnsNoDishes() = runTest {
            // Given invalid filter category.
            val filterCategory = Uuid.generateV4().toString()

            // When getDishes is invoked
            val result = menuRepository.getDishes(filterCategory = filterCategory)
                .filter { res -> res is Resource.Success }.first()

            // Then, the result is success with an empty list.
            assertTrue(result is Resource.Success)
            result as Resource.Success
            assertNotNull(result.data)
            assertEquals(0, result.data.size)
        }
    }

    @Nested
    inner class GetCategories {

        @Test
        fun dbFailure_throwsErrorResource() = runTest {

        }

        @Test
        fun dbSuccess_throwsSuccessWithData() = runTest {
        }

        @Test
        fun emptyDB_throwsSuccessWithEmptyList() = runTest {
        }
    }
}