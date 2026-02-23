package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetAddressCountUseCaseTest {

    private val repository = mockk<AddressRepository>()
    private val useCase = GetAddressCountUseCase(repository)

    @Test
    fun getAddressCountUseCase_repositoryReturnsZero_returnsZero() = runTest {
        // Given, the repository returns zero
        coEvery { repository.getAddressCount() } returns 0

        // Then, useCase returns zero
        assertEquals(0, useCase())
    }

    @Test
    fun getAddressCountUseCase_repositoryReturnsNonZero_returnsNonZero() = runTest {
        // Given, the repository returns five
        coEvery { repository.getAddressCount() } returns 5

        // Then, useCase returns five
        assertEquals(5, useCase())
    }

    @Test
    fun getAddressCountUseCase_repositoryReturnsNegativeOne_returnsNegativeOne() = runTest {
        // Given, the repository returns five
        coEvery { repository.getAddressCount() } returns -1

        // Then, useCase returns five
        assertEquals(-1, useCase())
    }

}