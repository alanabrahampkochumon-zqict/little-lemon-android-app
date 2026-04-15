package com.littlelemon.application.home.presentation

import app.cash.turbine.test
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.utils.AddressGenerator
import com.littlelemon.application.core.domain.utils.Resource
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
    private lateinit var viewModel: HomeViewModel
    private val addresses = List(3) { AddressGenerator.generateLocalAddress() }

    @BeforeEach
    fun setUp() {
        getAddressUseCase = mockk()

        coEvery { getAddressUseCase.invoke() } returns flow {
            emit(Resource.Loading(null))
            emit(Resource.Success(addresses))
        }

        viewModel = HomeViewModel(getAddressUseCase)
    }


    @Nested
    inner class AddressTests {

        @Test
        fun initiallyEmitsLoadingState() = runTest {
            viewModel.addresses.test {
                val loadingState = awaitItem()
                assertIs<Resource.Loading<List<LocalAddress>>>(loadingState)
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun onSuccess_emitsSuccessResourceWithData() = runTest {
            viewModel.addresses.test {
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
            viewModel = HomeViewModel(getAddressUseCase)

            viewModel.addresses.test {
                skipItems(1)

                // Then a failure state is emitted.
                val failureState = awaitItem()
                assertIs<Resource.Failure<List<LocalAddress>>>(failureState)
            }
        }
    }

}