package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.Location
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLocationUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: GetLocationUseCase
    private val location = Location(1.234, 2.342)

    @Before
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
        coEvery { repository.getLocation() } returns Resource.Success(data = location)

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Success)
        assertEquals(location, (result as Resource.Success).data)
    }


}