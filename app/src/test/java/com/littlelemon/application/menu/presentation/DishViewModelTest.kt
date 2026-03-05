package com.littlelemon.application.menu.presentation

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.data.mappers.toDish
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import com.littlelemon.application.menu.utils.DishEntityGenerator
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
class DishViewModelTest {

    @JvmField
    @RegisterExtension
    val coroutineRule = StandardTestDispatcherRule()

    private val testDispatcher = coroutineRule.testDispatcher

    private val dishes = DishEntityGenerator.generateDishWithCategories(10).map { it.toDish() }
    private val outOfStockDishes =
        DishEntityGenerator.generateDishWithCategories(5).map { it.toDish().copy(stock = 0) }
    private val remoteDishes =
        DishEntityGenerator.generateDishWithCategories(15).map { it.toDish() }

    private lateinit var useCase: GetDishesUseCase
    private lateinit var viewModel: DishViewModel

    @BeforeEach
    fun setUp() {
        useCase = mockk()

        coEvery { useCase.invoke() } returns flow {
            emit(Resource.Success(dishes))
        }
        coEvery { useCase.invoke(filter = DishFilter.INCLUDE_OUT_OF_STOCK) } returns flow {
            emit(
                Resource.Success(dishes + outOfStockDishes)
            )
        }
        coEvery { useCase.invoke(filter = null) } returns flow {
            emit(Resource.Success(dishes))
        }
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

        viewModel = DishViewModel(useCase, testDispatcher)
    }


    @Test
    fun dishViewModel_onInitialization_stateIsSetToLoading() = runTest {
        // Given viewmodel is created
        viewModel.state.test {
            // Then, initial state is loading
            assertTrue(awaitItem().isLoading)
        }

    }

    @Test
    fun dishViewModel_onInitialization_getsDishesFromUseCase() = runTest {
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

    @Test
    fun onApplyFilter_outOfStockFilter_emitsDishesIncludingOutOfStock() = runTest {
        viewModel.state.test {
            awaitItem() // Skip the initial loading

            // When, include out of stock filter is applied
            viewModel.onAction(DishActions.ApplyFiltering(filter = DishFilter.INCLUDE_OUT_OF_STOCK))

            // Then, the result contains dishes including out of stock dishes
            val state = awaitItem()
            assertEquals(dishes + outOfStockDishes, state.dishes)
        }
    }

    @Test
    fun onApplyFilter_noFilterApplied_emitsOnlyInStockDishes() = runTest {
        viewModel.state.test {
            awaitItem() // Skip the initial loading

            // When, include out of stock filter is applied
            viewModel.onAction(DishActions.ApplyFiltering(filter = null))

            // Then, the result contains dishes including out of stock dishes
            val state = awaitItem()
            assertEquals(dishes, state.dishes)
        }
    }

    // TODO: Implementation
//    @Test
//    fun onApplySorting_showsLoadingInitially() = runTest {
//        viewModel.state.test {
//            awaitItem() // Skip the initial loading
//
//            // When, sort is applied
//            viewModel.onAction(DishActions.ApplySorting(DishSorting.NAME_ASCENDING))
//            // Then, the initial state is loading
//            assertTrue(awaitItem().isLoading)
//        }
//    }

    @Test
    fun onApplySorting_sortedByNameAscending_emitsDishesSortedByNameAscending() = runTest {
        viewModel.state.test {
            awaitItem() // Skip the initial loading

            // When, sort by name ascending is applied
            viewModel.onAction(DishActions.ApplySorting(DishSorting.NAME_ASCENDING))

            // Then, the result contains dishes sorted by name ascending
            val state = awaitItem()
            assertNotNull(state.dishes)
            assertTrue(state.dishes.zipWithNext { firstDish, secondDish -> firstDish.title <= secondDish.title }
                .all { it })
            assertFalse(state.isLoading) // and isLoading is false
        }
    }

    @Test
    fun onApplySorting_sortedByNameDescending_emitsDishesSortedByNameAscending() = runTest {
        viewModel.state.test {
            awaitItem() // Skip the initial loading

            // When, sort by name ascending is applied
            viewModel.onAction(DishActions.ApplySorting(DishSorting.NAME_DESCENDING))

            // Then, the result contains dishes sorted by name ascending
            val state = awaitItem()
            assertNotNull(state.dishes)
            assertTrue(state.dishes.zipWithNext { firstDish, secondDish -> firstDish.title >= secondDish.title }
                .all { it })
            assertFalse(state.isLoading) // and isLoading is false
        }
    }

    @Test
    fun onFetchDishes_withFromRemote_fetchesItemFromRemote() = runTest {
        viewModel.state.test {
            awaitItem() // Skip the initial loading

            // When forceFetch is called
            viewModel.onAction(DishActions.FetchDishes(true))

            // Then, items are fetched from remote
            val state = awaitItem()
            assertNotNull(state.dishes)
            assertEquals(remoteDishes, state.dishes)
        }
    }

    @Test
    fun onFetchDishes_withoutFromRemote_fetchesItemFromCache() = runTest {
        viewModel.state.test {
            awaitItem() // Skip the initial loading

            // When forceFetch is called
            viewModel.onAction(DishActions.FetchDishes(false))

            // Then, items are fetched from cache
            val state = awaitItem()
            assertNotNull(state.dishes)
            assertEquals(dishes, state.dishes)
        }
    }

}