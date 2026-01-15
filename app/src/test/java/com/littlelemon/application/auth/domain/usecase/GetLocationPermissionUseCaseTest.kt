package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.core.domain.utils.Error
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLocationPermissionUseCaseTest {

    private lateinit var useCase: GetLocationPermissionUseCase
    private lateinit var repository: AuthRepository

    @BeforeEach
    fun setUp() {
        repository = mockk<AuthRepository>()
        useCase = GetLocationPermissionUseCase(repository)
    }

    @Test
    fun givenRepositoryReturnsPermissionDenied_useCaseReturns_failure() = runTest {
        // Arrange
        val permissionDenied = Error.PermissionDenied
        coEvery { repository.getLocationPermission() } returns Resource.Failure(error = permissionDenied)

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Failure)
        assertEquals(permissionDenied, (result as Resource.Failure).error)
    }

    @Test
    fun givenRepositoryReturnsPermissionGranted_useCaseReturns_success() = runTest {
        // Arrange
        coEvery { repository.getLocationPermission() } returns Resource.Success()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Success)
    }

}