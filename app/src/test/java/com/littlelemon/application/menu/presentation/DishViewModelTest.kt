package com.littlelemon.application.menu.presentation

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.data.mappers.toDish
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.utils.DishEntityGenerator
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@SmallTest
@ExtendWith(StandardTestDispatcherRule::class)
class DishViewModelTest {

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

        coEvery { useCase.invoke() } returns flow { emit(Resource.Success(dishes)) }
        coEvery { useCase.invoke(filter = null) } returns flow { emit(Resource.Success(dishes)) }
        coEvery { useCase.invoke(filter = DishFilter.INCLUDE_OUT_OF_STOCK) } returns flow {
            emit(
                Resource.Success(dishes + outOfStockDishes)
            )
        }

        viewModel = DishViewModel(useCase)
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

//    @Test
//    fun onApplyFilter_filterByNameAscending_emitsDishesFilteredByNameAscending() = runTest {
//
//    }

}