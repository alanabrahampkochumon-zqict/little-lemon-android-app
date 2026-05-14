package com.littlelemon.application.orders.presentation.screens

import app.cash.turbine.test
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import com.littlelemon.application.shared.cart.domain.usecase.GetCartItemDetailsUseCase
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.random.Random
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@ExtendWith(StandardTestDispatcherRule::class)
class OrderScreenViewModelTest {

    private lateinit var getItemUseCase: GetCartItemDetailsUseCase
    private lateinit var viewModel: OrderScreenViewModel
    private lateinit var testScope: TestScope


    private val cartDetailItems = List(5) {
        val dish = DishGenerator.generateDish()
        CartDetailItem(dish, Random.nextInt(3, 5))
    }

    private val cartDetailItemSharedFlow = MutableSharedFlow<List<CartDetailItem>>(replay = 1)


    @BeforeEach
    fun setUp() {
        getItemUseCase = mockk()

        testScope = TestScope(StandardTestDispatcher())

        coEvery { getItemUseCase.invoke() } returns cartDetailItemSharedFlow.asSharedFlow()
        testScope.launch { cartDetailItemSharedFlow.emit(cartDetailItems) }

        viewModel =
            OrderScreenViewModel(
                getItemUseCase
            )

    }

    @Nested
    inner class StateTests {

        @Test
        fun isLoadingIsTrue_initially() = runTest {
            viewModel.state.test {
                val initialState = awaitItem()
                assertTrue { initialState.isCartItemLoading }
                assertEquals(0, initialState.cartItems.size)
                assertNull(initialState.cartItemError)
            }
        }

        @Test
        fun isLoadingIsFalse_afterFirstEmission() = runTest {
            viewModel.state.test {
                skipItems(1)
                val state = awaitItem()
                assertFalse { state.isCartItemLoading }
            }
        }

        @Test
        fun dataIsLoaded_afterInitialLoad() = runTest {
            viewModel.state.test {
                skipItems(1) // Initial state
                val state = awaitItem()
                assertEquals(cartDetailItems, state.cartItems)
            }
        }
    }

}