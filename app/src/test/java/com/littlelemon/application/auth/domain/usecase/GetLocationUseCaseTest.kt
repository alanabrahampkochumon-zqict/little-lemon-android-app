package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.LocalLocation
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLocationUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: GetLocationUseCase
    private val localLocation = LocalLocation(1.234, 2.342)

    @BeforeEach
    fun setUp() {
        repository = mockk<AuthRepository>()
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
        assertTrue(result is Resource.Failure)
        assertEquals(message, (result as Resource.Failure).errorMessage)
    }

    @Test
    fun givenLocationReceived_useCaseReturns_successWithData() = runTest {
        // Arrange
        coEvery { repository.getLocation() } returns Resource.Success(data = localLocation)

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Success)
        assertEquals(localLocation, (result as Resource.Success).data)
    }


}