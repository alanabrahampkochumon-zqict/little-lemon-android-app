package com.littlelemon.application.shared.cart.domain.usecase

import app.cash.turbine.test
import com.littlelemon.application.cart.data.FakeCartRepository
import com.littlelemon.application.shared.cart.domain.CartRepository
import com.littlelemon.application.shared.cart.domain.usecase.GetCartErrorMessagesUseCase
import com.littlelemon.application.shared.cart.domain.usecase.GetCartItemsUseCase
import com.littlelemon.application.utils.StandardTestDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(StandardTestDispatcherRule::class)
class GetCartErrorMessagesUseCaseTest {

    private lateinit var repository: CartRepository
    private lateinit var useCase: GetCartErrorMessagesUseCase


    @Test
    fun onError_messageAreEmitted() = runTest {
        // Given a repository that throws error
        repository = FakeCartRepository(throwError = true)
        useCase = GetCartErrorMessagesUseCase(repository)
        val getItem = GetCartItemsUseCase(repository)

        useCase().test {
            // When, a function is invoked that emits events
            val items = getItem().first()
            assertEquals(0, items.size)

            // Then, an error event is emitted
            val message = awaitItem()
            assertEquals(FakeCartRepository.ERROR_MESSAGE, message)
        }

    }
}