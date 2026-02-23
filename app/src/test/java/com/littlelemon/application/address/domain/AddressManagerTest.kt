package com.littlelemon.application.address.domain

import com.littlelemon.application.address.domain.usecase.GetAddressCountUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddressManagerTest {

    private val useCase = mockk<GetAddressCountUseCase>()
    private val addressManager = AddressManagerImpl(useCase)

    @Test
    fun userHasAddress_useCaseReturnsNegativeOne_returnsFalse() = runTest {
        // Given, use case returns -1
        coEvery { useCase.invoke() } returns -1

        // Then, userHasAddress returns false
        assertFalse(addressManager.userHasAddress())
    }

    @Test
    fun userHasAddress_useCaseReturnsZero_returnsFalse() = runTest {
        // Given, use case returns 0
        coEvery { useCase.invoke() } returns 0

        // Then, userHasAddress returns false
        assertFalse(addressManager.userHasAddress())
    }

    @Test
    fun userHasAddress_useCaseReturnsNonZeroPositiveNumber_returnsTrue() = runTest {
        // Given, use case returns -1
        coEvery { useCase.invoke() } returns 5

        // Then, userHasAddress returns false
        assertTrue(addressManager.userHasAddress())
    }

}