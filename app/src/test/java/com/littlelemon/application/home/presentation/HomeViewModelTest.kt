package com.littlelemon.application.home.presentation

import app.cash.turbine.test
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.utils.AddressGenerator
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.domain.usecase.GetCategoriesUseCase
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(StandardTestDispatcherRule::class)
class HomeViewModelTest {

    private lateinit var getAddressUseCase: GetAddressUseCase
    private lateinit var getDishesUseCase: GetDishesUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var viewModel: HomeViewModel
    private val addresses = List(3) { AddressGenerator.generateLocalAddress() }
    private val dishes = List(5) { DishGenerator.generateDish() }
    private val categories = dishes.flatMap { dish -> dish.category }.distinct()

    @BeforeEach
    fun setUp() {
        getAddressUseCase = mockk()
        getDishesUseCase = mockk()
        getCategoriesUseCase = mockk()

        coEvery { getAddressUseCase.invoke() } returns flow {
            emit(Resource.Loading(null))
            emit(Resource.Success(addresses))
        }

        coEvery { getDishesUseCase.invoke(any(), any(), any(), any()) } returns flow {
            emit(Resource.Loading(null))
            emit(Resource.Success(dishes))
        }

        coEvery { getCategoriesUseCase.invoke() } returns flow {
            emit(Resource.Loading(null))
            emit(Resource.Success(categories))
        }

        viewModel = HomeViewModel(getAddressUseCase, getDishesUseCase, getCategoriesUseCase)
    }


    @Nested
    inner class AddressState {

        @Test
        fun initiallyEmitsLoadingState() = runTest {
            viewModel.state.test {
                val state = awaitItem()
                assertTrue { state.addressLoading }
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun onSuccess_emitsSuccessResourceWithData() = runTest {
            viewModel.state.test {
                skipItems(1)

                val successState = awaitItem()
                assertEquals(addresses, successState.addresses)
                assertNull(successState.addressError)
                Assertions.assertFalse(successState.addressLoading)
            }
        }

        @Test
        fun onFailure_emitsFailureResource() = runTest {
            // When a usecase failure occurs
            coEvery { getAddressUseCase.invoke() } returns flow { emit(Resource.Failure()) }
            viewModel = HomeViewModel(getAddressUseCase, getDishesUseCase, getCategoriesUseCase)

            viewModel.state.test {
                skipItems(1)

                val failureState = awaitItem()
                // Then error state is not null
                assertNotNull(failureState.addressError)
                // And loading state is false
                Assertions.assertFalse(failureState.addressLoading)
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Nested
    inner class DishesState {

        @Test
        fun initiallyEmitsLoadingState() = runTest {
            viewModel.state.test {
                val state = awaitItem()
                assertTrue { state.dishLoading }
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun onSuccess_emitsSuccessResourceWithData() = runTest {
            viewModel.state.test {
                skipItems(1)

                val successState = awaitItem()
                assertEquals(dishes, successState.popularDishes)
                assertNull(successState.dishError)
                Assertions.assertFalse(successState.dishLoading)
            }
        }

        @Test
        fun onFailure_emitsFailureResource() = runTest {
            // When a usecase failure occurs
            coEvery { getDishesUseCase.invoke() } returns flow { emit(Resource.Failure()) }
            viewModel = HomeViewModel(getAddressUseCase, getDishesUseCase, getCategoriesUseCase)

            viewModel.state.test {
                skipItems(1)

                val failureState = awaitItem()
                // Then error state is not null
                assertNotNull(failureState.dishError)
                // And loading state is false
                Assertions.assertFalse(failureState.dishLoading)
                cancelAndConsumeRemainingEvents()
            }
        }
    }


    @Nested
    inner class CategoryState {

        @Test
        fun initiallyEmitsLoadingState() = runTest {
            viewModel.state.test {
                val state = awaitItem()
                assertTrue { state.categoryLoading }
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun onSuccess_emitsSuccessResourceWithData() = runTest {
            viewModel.state.test {
                skipItems(1)

                val successState = awaitItem()
                assertEquals(categories, successState.categories)
                assertNull(successState.categoryError)
                Assertions.assertFalse(successState.categoryLoading)
            }
        }

        @Test
        fun onFailure_emitsFailureResource() = runTest {
            // When a usecase failure occurs
            coEvery { getCategoriesUseCase.invoke() } returns flow { emit(Resource.Failure()) }
            viewModel = HomeViewModel(getAddressUseCase, getDishesUseCase, getCategoriesUseCase)

            viewModel.state.test {
                skipItems(1)

                val failureState = awaitItem()
                // Then error state is not null
                assertNotNull(failureState.categoryError)
                // And loading state is false
                Assertions.assertFalse(failureState.categoryLoading)
                cancelAndConsumeRemainingEvents()
            }
        }
    }
}