package com.littlelemon.application.cart.presentation

import app.cash.turbine.test
import com.littlelemon.application.cart.domain.models.CartItem
import com.littlelemon.application.cart.domain.usecase.ClearCartUseCase
import com.littlelemon.application.cart.domain.usecase.GetCartErrorMessagesUseCase
import com.littlelemon.application.cart.domain.usecase.GetCartItemsUseCase
import com.littlelemon.application.cart.domain.usecase.UpsertCartItemUseCase
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.random.Random
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(StandardTestDispatcherRule::class)
class CartViewModelTest {

    private lateinit var errorMessageUseCase: GetCartErrorMessagesUseCase
    private lateinit var getItemUseCase: GetCartItemsUseCase
    private lateinit var upsertUseCase: UpsertCartItemUseCase
    private lateinit var clearCartUseCase: ClearCartUseCase

    private lateinit var testScope: TestScope

    private lateinit var viewModel: CartViewModel


    private val errorMessage = "ERROR MESSAGE"
    private val cartItems = List(5) {
        val dish = DishGenerator.generateDish()
        CartItem(dish, Random.nextInt(3, 5))
    }


    @BeforeEach
    fun setUp() {
        errorMessageUseCase = mockk()
        getItemUseCase = mockk()
        upsertUseCase = mockk()
        clearCartUseCase = mockk()

        testScope = TestScope(StandardTestDispatcher())

        coEvery { errorMessageUseCase.invoke() } returns flow { emit(errorMessage) }.shareIn(
            testScope,
            SharingStarted.Eagerly, 1
        )
        coEvery { getItemUseCase.invoke() } returns flow { emit(cartItems) }
        coEvery { upsertUseCase(any()) } returns Unit
        coEvery { clearCartUseCase.invoke() } returns Resource.Success()

        viewModel =
            CartViewModel(getItemUseCase, errorMessageUseCase, upsertUseCase, clearCartUseCase)

    }

    @Nested
    inner class ErrorState {

        @Test
        fun emitsErrorState_whenErrorMessageIsInQueue() = runTest(testScope.testScheduler) {
            viewModel.errorState.test {
                val message = awaitItem()
                assertEquals(errorMessage, message)
            }
        }
    }

    @Nested
    inner class StateTest {

        @Test
        fun isLoadingIsTrue_initially() = runTest {
            viewModel.state.test {
                val initialState = awaitItem()
                assertTrue { initialState.isLoading }
                assertEquals(0, initialState.cartItems.size)
                assertNull(initialState.errorMessage)
            }
        }

        @Test
        fun isLoadingIsFalse_afterFirstEmission() = runTest {
            viewModel.state.test {
//                skipItems(1) // Initial state
                println(awaitItem())
                val state = awaitItem()
                assertFalse { state.isLoading }
            }
        }

        @Test
        fun errorMessages_areEmittedAfterInitialLoad() = runTest {
            viewModel.state.test {
                skipItems(1) // Initial state
                val state = awaitItem()
                assertNotNull { state.errorMessage }
            }
        }

        @Test
        fun dataIsLoaded_afterInitialLoad() = runTest {
            viewModel.state.test {
                skipItems(1) // Initial state
                val state = awaitItem()
                assertEquals(cartItems, state.cartItems)
            }
        }
    }

}