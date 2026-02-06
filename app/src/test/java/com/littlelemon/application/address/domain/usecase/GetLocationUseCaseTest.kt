package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.data.AddressRepositoryImpl
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLocationUseCaseTest {
    private lateinit var repository: AddressRepository
    private lateinit var useCase: GetLocationUseCase
    private val localLocation = LocalLocation(1.234, 2.342)

    @BeforeEach
    fun setUp() {
        repository = mockk<AddressRepositoryImpl>()
        useCase = GetLocationUseCase(repository)
    }

    @Test
    fun givenLocationNotReceived_useCaseReturns_failure() = runTest {
        // Arrange
        val message = "error"
        coEvery { repository.getLocation() } returns Resource.Failure(errorMessage = message)

        // Act
        val result = useCase()

        // Assert
        Assertions.assertTrue(result is Resource.Failure)
        Assertions.assertEquals(message, (result as Resource.Failure).errorMessage)
    }

    @Test
    fun givenLocationReceived_useCaseReturns_successWithData() = runTest {
        // Arrange
        coEvery { repository.getLocation() } returns Resource.Success(data = localLocation)

        // Act
        val result = useCase()

        // Assert
        Assertions.assertTrue(result is Resource.Success)
        Assertions.assertEquals(localLocation, (result as Resource.Success).data)
    }


}