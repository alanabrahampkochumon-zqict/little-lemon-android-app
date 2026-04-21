package com.littlelemon.application.home.presentation

import app.cash.turbine.test
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.utils.AddressGenerator
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.domain.usecase.GetCategoriesUseCase
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HomeViewModelTest {

    private lateinit var getAddressUseCase: GetAddressUseCase
    private lateinit var getDishesUseCase: GetDishesUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var viewModel: HomeViewModel
    private val addresses = List(3) { AddressGenerator.generateLocalAddress() }

    @BeforeEach
    fun setUp() {
        getAddressUseCase = mockk()
        getDishesUseCase = mockk()
        getCategoriesUseCase = mockk()

        coEvery { getAddressUseCase.invoke() } returns flow {
            emit(Resource.Loading(null))
            emit(Resource.Success(addresses))
        }

        viewModel = HomeViewModel(getAddressUseCase, getDishesUseCase, getCategoriesUseCase)
    }


    @Nested
    inner class AddressTests {

        @Test
        fun initiallyEmitsLoadingState() = runTest {
            viewModel.state.test {
                val loadingState = awaitItem()
//                assertTrue { loadingState.is }
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun onSuccess_emitsSuccessResourceWithData() = runTest {
            viewModel.state.test {
                skipItems(1)

                val successState = awaitItem()
                assertIs<Resource.Success<List<LocalAddress>>>(successState)
                assertEquals(addresses, successState.data)
            }
        }

        @Test
        fun onFailure_emitsFailureResource() = runTest {
            // When a usecase failure occurs
            coEvery { getAddressUseCase.invoke() } returns flow { emit(Resource.Failure()) }
            viewModel = HomeViewModel(getAddressUseCase, getDishesUseCase, getCategoriesUseCase)

            viewModel.state.test {
                skipItems(1)

                // Then a failure state is emitted.
                val failureState = awaitItem()
                assertIs<Resource.Failure<List<LocalAddress>>>(failureState)
            }
        }
    }

}