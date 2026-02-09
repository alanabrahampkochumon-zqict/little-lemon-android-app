package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.utils.AddressGenerator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

class GetAddressUseCaseTest {


    private val repository: AddressRepository = mockk()
    private val useCase = GetAddressUseCase(repository)

    @Test
    fun onGetAddress_repositorySuccess_returnsResourceSuccess() = runTest {
        // Arrange
        val numAddress = 2
        val address = List(numAddress) { AddressGenerator.generateLocalAddress() }
        coEvery { repository.getAddress() } returns flow { emit(Resource.Success(address)) }

        // Act
        val result = useCase().first()

        // Assert
        assertTrue(result is Resource.Success)
        result as Resource.Success

        assertNotNull(result.data)
        assertEquals(numAddress, result.data.size)
        assertEquals(address, result.data)
    }

    @Test
    fun onGetAddress_repositoryFailure_returnsResourceSuccess() = runTest {
        // Arrange
        val errorMessage = "unknown error"
        coEvery { repository.getAddress() } returns flow { emit(Resource.Failure(errorMessage = errorMessage)) }

        // Act
        val result = useCase().first()

        // Assert
        assertTrue(result is Resource.Failure)
        result as Resource.Failure
        assertNull(result.data)
        assertEquals(errorMessage, result.errorMessage)
    }
}