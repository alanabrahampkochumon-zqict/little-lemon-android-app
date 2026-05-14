package com.littlelemon.application.orders.presentation.screens

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.utils.AddressGenerator
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import com.littlelemon.application.shared.cart.domain.usecase.GetCartItemDetailsUseCase
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
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
class OrderScreenViewModelTest {

    private lateinit var getItemUseCase: GetCartItemDetailsUseCase
    private lateinit var getAddressUseCase: GetAddressUseCase
    private lateinit var viewModel: OrderScreenViewModel
    private lateinit var testScope: TestScope


    private val cartDetailItems = List(5) {
        val dish = DishGenerator.generateDish()
        CartDetailItem(dish, Random.nextInt(3, 5))
    }

    private val defaultAddress = AddressGenerator.generateLocalAddress().copy(isDefault = true)
    private val addresses =
        List(3) { AddressGenerator.generateLocalAddress().copy(isDefault = false) } + listOf(
            defaultAddress
        )

    private val cartDetailItemSharedFlow = MutableSharedFlow<List<CartDetailItem>>(replay = 1)


    @BeforeEach
    fun setUp() {
        getItemUseCase = mockk()
        getAddressUseCase = mockk()

        testScope = TestScope(StandardTestDispatcher())

        coEvery { getItemUseCase.invoke() } returns cartDetailItemSharedFlow.asSharedFlow()
        testScope.launch { cartDetailItemSharedFlow.emit(cartDetailItems) }

        coEvery { getAddressUseCase.invoke() } returns flow { emit(Resource.Success(addresses)) }


        viewModel = OrderScreenViewModel(
            getItemUseCase, getAddressUseCase
        )

    }

    @Nested
    inner class CartItemStateTests {

        @Test
        fun isLoadingIsTrue_initially() = runTest {
            viewModel.state.test {
                val initialState = awaitItem()
                assertTrue { initialState.isCartItemLoading }
                assertTrue { initialState.isAddressLoading }
                assertEquals(0, initialState.cartItems.size)
                assertNull(initialState.cartItemError)
                assertNull(initialState.addressError)
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


    @Nested
    inner class AddressStateTests {
        @Test
        fun isLoadingIsTrue_initially() = runTest {
            viewModel.state.test {
                val initialState = awaitItem()
                assertTrue { initialState.isAddressLoading }
                assertNull(initialState.defaultAddress)
                assertNull(initialState.addressError)
            }
        }

        @Test
        fun isLoadingIsFalse_afterFirstEmission() = runTest {
            viewModel.state.test {
                skipItems(1)
                assertFalse { awaitItem().isAddressLoading }
            }
        }

        @Test
        fun retrievesDataAfterInitialLoad() = runTest {
            viewModel.state.test {
                skipItems(1)

                assertEquals(defaultAddress, awaitItem().defaultAddress)
            }
        }

        @Test
        fun useCaseError_throwsErrorMessage() = runTest {
            turbineScope {
                coEvery { getAddressUseCase.invoke() } returns flow {
                    emit(
                        Resource.Failure()
                    )
                }

                viewModel = OrderScreenViewModel(
                    getItemUseCase, getAddressUseCase
                )
                viewModel.state.test {
                    skipItems(1)
                    val state = awaitItem()
                    assertFalse { state.isAddressLoading }
                    assertNotNull(state.addressError)
                    assertNull(state.defaultAddress)
                }
            }
        }
    }

}