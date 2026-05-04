package com.littlelemon.application.cart.presentation

import app.cash.turbine.test
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import com.littlelemon.application.shared.cart.domain.usecase.ClearCartUseCase
import com.littlelemon.application.shared.cart.domain.usecase.GetCartErrorMessagesUseCase
import com.littlelemon.application.shared.cart.domain.usecase.GetCartItemDetailsUseCase
import com.littlelemon.application.shared.cart.domain.usecase.UpsertCartItemUseCase
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
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
    private lateinit var getItemUseCase: GetCartItemDetailsUseCase
    private lateinit var upsertUseCase: UpsertCartItemUseCase
    private lateinit var clearCartUseCase: ClearCartUseCase

    private lateinit var testScope: TestScope

    private lateinit var viewModel: CartViewModel


    private val errorMessage = "ERROR MESSAGE"
    private val cartDetailItems = List(5) {
        val dish = DishGenerator.generateDish()
        CartDetailItem(dish, Random.nextInt(3, 5))
    }

    private val cartDetailItemSharedFlow = MutableSharedFlow<List<CartDetailItem>>(replay = 1)


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

        coEvery { getItemUseCase.invoke() } returns cartDetailItemSharedFlow.asSharedFlow()
        testScope.launch { cartDetailItemSharedFlow.emit(cartDetailItems) }
        coEvery { upsertUseCase(any()) } returns Unit
        coEvery { clearCartUseCase.invoke() } returns Resource.Success()

        viewModel =
            CartViewModel(getItemUseCase, errorMessageUseCase, upsertUseCase, clearCartUseCase)

    }

    @Nested
    inner class StateTests {

        @Test
        fun isLoadingIsTrue_initially() = runTest {
            viewModel.state.test {
                val initialState = awaitItem()
                assertTrue { initialState.isLoading }
                assertEquals(0, initialState.cartDetailItems.size)
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
                assertEquals(cartDetailItems, state.cartDetailItems)
            }
        }
    }


    @Nested
    inner class CartActionTests {

        @Nested
        inner class IncreaseQuantityTests {

            @Test
            fun increasesQuantityOfTheCartItem() = runTest {
                val cartItem = cartDetailItems.first()
                viewModel.state.test {
                    skipItems(1)
                    // Initially the quantity equals the quantity in our initial cart item
                    val initialItem = awaitItem()
                    assertEquals(cartItem.quantity, initialItem.cartDetailItems.first().quantity)

                    // When increase quantity action is performed
                    viewModel.onAction(CartAction.IncreaseQuantity(cartItem))
                    // Since we are mocking the usecase, the flow is not a hot path, i.e, it will only emit the values returned from mock's coEvery
                    cartDetailItemSharedFlow.emit(
                        listOf(cartItem.copy(quantity = cartItem.quantity + 1)) + cartDetailItems.drop(
                            1
                        )
                    )
                    // Then, the quantity is incremented by 1
                    val finalItem = awaitItem()
                    assertEquals(cartItem.quantity + 1, finalItem.cartDetailItems.first().quantity)
                }
            }
        }

        @Nested
        inner class DecreaseQuantityTests {

            @Test
            fun decreasesQuantityOfTheCartItem() = runTest(testScope.testScheduler) {
                val cartItem = cartDetailItems.first()
                viewModel.state.test {
                    println(awaitItem())
                    // Initially the quantity equals the quantity in our initial cart item
                    val initialItem = awaitItem()
                    println(initialItem)
                    assertEquals(cartItem.quantity, initialItem.cartDetailItems.first().quantity)

                    // When decrease quantity action is performed
                    viewModel.onAction(CartAction.DecreaseQuantity(cartItem))
                    // Since we are mocking the usecase, the flow is not a hot path, i.e, it will only emit the values returned from mock's coEvery
                    cartDetailItemSharedFlow.emit(
                        listOf(cartItem.copy(quantity = cartItem.quantity - 1)) + cartDetailItems.drop(
                            1
                        )
                    )

                    // Then, the quantity is decremented by 1
                    val finalItem = awaitItem()
                    assertEquals(cartItem.quantity - 1, finalItem.cartDetailItems.first().quantity)
                }
            }
        }


        @Nested
        inner class RemoveItemTests {

            @Test
            fun removesItem() = runTest(testScope.testScheduler) {
                val cartItem = cartDetailItems.first()
                viewModel.state.test {
                    println(awaitItem())
                    // Initially the quantity equals the quantity in our initial cart item
                    val initialItem = awaitItem()
                    println(initialItem)
                    assertEquals(cartItem.quantity, initialItem.cartDetailItems.first().quantity)

                    // When decrease quantity action is performed
                    viewModel.onAction(CartAction.RemoveItem(cartItem))
                    // Since we are mocking the usecase, the flow is not a hot path, i.e, it will only emit the values returned from mock's coEvery
                    cartDetailItemSharedFlow.emit(
                        cartDetailItems.drop(
                            1
                        )
                    )

                    // Then, the quantity is decremented by 1
                    val finalItem = awaitItem()
                    assertFalse(finalItem.cartDetailItems.contains(cartItem))
                }
            }
        }
    }
}