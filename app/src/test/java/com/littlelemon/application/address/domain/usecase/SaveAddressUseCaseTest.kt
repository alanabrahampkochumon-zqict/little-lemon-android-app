package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.utils.AddressGenerator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SaveAddressUseCaseTest {

    val numAddress = 5
    val address = AddressGenerator.generateLocalAddress()

    private val repository: AddressRepository = mockk()
    private val useCase = SaveAddressUseCase(repository)

    @Test
    fun onSaveAddress_repositorySuccess_returnsResourceSuccess() = runTest {
        // Arrange
        coEvery { repository.saveAddress(address) } returns Resource.Success()

        // Act
        val result = useCase(address)

        // Assert
        assertTrue(result is Resource.Success)
    }

    @Test
    fun onSaveAddress_repositoryFailure_returnsResourceFailure() = runTest {
        // Arrange
        coEvery { repository.saveAddress(address) } returns Resource.Failure()

        // Act
        val result = useCase(address)

        // Assert
        assertTrue(result is Resource.Failure)
    }
}