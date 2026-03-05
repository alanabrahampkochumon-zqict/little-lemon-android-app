package com.littlelemon.application.menu.presentation

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.data.mappers.toDish
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
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
    private val remoteDishes =
        DishEntityGenerator.generateDishWithCategories(15).map { it.toDish() }

    private lateinit var useCase: GetDishesUseCase
    private lateinit var viewModel: DishViewModel

    @BeforeEach
    fun setUp() {
        useCase = mockk()

        coEvery { useCase.invoke() } returns flow { emit(Resource.Success(dishes)) }

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

}