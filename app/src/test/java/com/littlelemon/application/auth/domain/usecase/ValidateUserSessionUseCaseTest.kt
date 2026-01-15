package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class ValidateUserSessionUseCaseTest {
    private lateinit var useCase: ValidateUserSessionUseCase
    private lateinit var repository: AuthRepository

    @BeforeEach
    fun setUp() {
        repository = mockk<AuthRepository>()
        useCase = ValidateUserSessionUseCase(repository)
    }

    @Test
    fun givenValidAccessToken_useCaseReturns_success() = runTest {
        // Arrange
        val token = SessionToken(
            accessToken = UUID.randomUUID().toString(),
            accessTokenExpiry = LocalDateTime.now().plusDays(2),
            refreshToken = UUID.randomUUID().toString(),
            refreshTokenExpiry = LocalDateTime.now().plusDays(4)
        )
        coEvery { repository.getUserSession() } returns Resource.Success(token)

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Success)
        val data = (result as Resource.Success).data
        assertEquals(token, data)
    }

    @Test
    fun givenNullButSuccessResult_useCaseReturns_failure() = runTest {
        // Arrange
        val token = null
        coEvery { repository.getUserSession() } returns Resource.Success(token)

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Failure)
    }


    @Test
    fun givenFailedResult_useCaseReturns_failure() = runTest {
        // Arrange
        coEvery { repository.getUserSession() } returns Resource.Failure()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Failure)
    }

}