package com.littlelemon.application.menu.presentation

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.data.mappers.toDish
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
class MenuViewModelTest {

    @JvmField
    @RegisterExtension
    val coroutineRule = StandardTestDispatcherRule()

    private val testDispatcher = coroutineRule.testDispatcher

    private val dishes = DishGenerator.generateDishWithCategories(10)
        .map { (dishEntity, _) -> dishEntity.toDish() }
    private val outOfStockDishes =
        DishGenerator.generateDishWithCategories(5)
            .map { (dishEntity, _) -> dishEntity.toDish().copy(stock = 0) }
    private val remoteDishes =
        DishGenerator.generateDishWithCategories(15)
            .map { (dishEntity, _) -> dishEntity.toDish() }
    private lateinit var useCase: GetDishesUseCase
    private lateinit var viewModel: MenuViewModel

    @BeforeEach
    fun setUp() {
        useCase = mockk()

        coEvery { useCase.invoke() } returns flow {
            emit(Resource.Success(dishes))
        }

        viewModel = MenuViewModel(useCase, testDispatcher)
    }


    @Nested
    inner class ViewModelInitialization {
        @Test
        fun initialStateIsSetToLoading() = runTest {
            // Given viewmodel is created
            viewModel.state.test {
                // Then, initial state is loading
                assertTrue(awaitItem().isLoading)
            }

        }

        @Test
        fun getsDishesFromUseCase() = runTest {
            // Given viewmodel is created
            viewModel.state.test {
                awaitItem() // Skips the initial loading

                // Then, then result is populated
                val state = awaitItem()

                assertFalse(state.isLoading)
                assertNull(state.error)
                assertEquals(dishes, state.dishes)
            }
        }
    }


    @Nested
    inner class Actions {

        @BeforeEach
        fun setUp() {
            coEvery { useCase.invoke(forceFetch = true) } returns flow {
                emit(
                    Resource.Success(remoteDishes)
                )
            }

            coEvery { useCase.invoke(forceFetch = false) } returns flow {
                emit(
                    Resource.Success(dishes)
                )
            }
            viewModel = MenuViewModel(useCase, testDispatcher)
        }

        @Nested
        inner class ApplyFilter {

            @BeforeEach
            fun setUp() {
                coEvery { useCase.invoke(filter = DishFilter.INCLUDE_OUT_OF_STOCK) } returns flow {
                    emit(
                        Resource.Success(dishes + outOfStockDishes)
                    )
                }
                coEvery { useCase.invoke(filter = null) } returns flow {
                    emit(Resource.Success(dishes))
                }
                viewModel = MenuViewModel(useCase, testDispatcher)
            }

            @Test
            fun outOfStockFilter_emitsDishesIncludingOutOfStock() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When, include out of stock filter is applied
                    viewModel.onAction(MenuActions.ApplyFiltering(filter = DishFilter.INCLUDE_OUT_OF_STOCK))

                    // Then, the result contains dishes including out of stock dishes
                    val state = awaitItem()
                    assertEquals(dishes + outOfStockDishes, state.dishes)
                }
            }

            @Test
            fun noFilterApplied_emitsOnlyInStockDishes() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When, include out of stock filter is applied
                    viewModel.onAction(MenuActions.ApplyFiltering(filter = null))

                    // Then, the result contains dishes including out of stock dishes
                    val state = awaitItem()
                    assertEquals(dishes, state.dishes)
                }
            }
        }

        @Nested
        inner class ApplySorting {

            @BeforeEach
            fun setUp() {
                coEvery { useCase.invoke(sorting = DishSorting.NAME_ASCENDING) } returns flow {
                    emit(
                        Resource.Success(dishes.sortedBy { dish -> dish.title })
                    )
                }
                coEvery { useCase.invoke(sorting = DishSorting.NAME_DESCENDING) } returns flow {
                    emit(
                        Resource.Success(dishes.sortedByDescending { dish -> dish.title })
                    )
                }
                viewModel = MenuViewModel(useCase, testDispatcher)
            }


            @Test
            fun sortedByNameAscending_emitsDishesSortedByNameAscending() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When, sort by name ascending is applied
                    viewModel.onAction(MenuActions.ApplySorting(DishSorting.NAME_ASCENDING))

                    // Then, the result contains dishes sorted by name ascending
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertTrue(state.dishes.zipWithNext { firstDish, secondDish -> firstDish.title <= secondDish.title }
                        .all { it })
                    assertFalse(state.isLoading) // and isLoading is false
                }
            }

            @Test
            fun sortedByNameDescending_emitsDishesSortedByNameAscending() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When, sort by name ascending is applied
                    viewModel.onAction(MenuActions.ApplySorting(DishSorting.NAME_DESCENDING))

                    // Then, the result contains dishes sorted by name ascending
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertTrue(state.dishes.zipWithNext { firstDish, secondDish -> firstDish.title >= secondDish.title }
                        .all { it })
                    assertFalse(state.isLoading) // and isLoading is false
                }
            }
        }

        @Nested
        inner class FetchDishes {
            @Test
            fun withFromRemote_fetchesItemFromRemote() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When forceFetch is called
                    viewModel.onAction(MenuActions.FetchDishes(true))

                    // Then, items are fetched from remote
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertEquals(remoteDishes, state.dishes)
                }
            }

            @Test
            fun withoutFromRemote_fetchesItemFromCache() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When forceFetch is called
                    viewModel.onAction(MenuActions.FetchDishes(false))

                    // Then, items are fetched from cache
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertEquals(dishes, state.dishes)
                }
            }
        }

        @Nested
        inner class UpdateCategory {
            private val filterCategory = "CATEGORY"
            private val categoryFilteredDishes =
                DishGenerator.generateDishWithCategories(5)
                    .map { (dishEntity, _) -> dishEntity.toDish() }

            @BeforeEach
            fun setUp() {
                coEvery { useCase.invoke(filterCategory = null) } returns flow {
                    emit(Resource.Success(dishes))
                }

                coEvery { useCase.invoke(filterCategory = filterCategory) } returns flow {
                    emit(Resource.Success(categoryFilteredDishes))
                }

                viewModel = MenuViewModel(useCase, testDispatcher)
            }


            @Test
            fun nullCategory_returnsAllDishes() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When UpdateDishCategoryAction is triggered null category
                    viewModel.onAction(MenuActions.UpdateDishCategory(null))

                    // Then, the original dishes are emitted
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertEquals(dishes, state.dishes)
                }
            }

            @Test
            fun validCategory_returnsFilteredDishes() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When UpdateDishCategoryAction is triggered null category
                    viewModel.onAction(MenuActions.UpdateDishCategory(filterCategory))

                    // Then, the filtered dishes are emitted
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertEquals(categoryFilteredDishes, state.dishes)
                }
            }
        }
    }

}