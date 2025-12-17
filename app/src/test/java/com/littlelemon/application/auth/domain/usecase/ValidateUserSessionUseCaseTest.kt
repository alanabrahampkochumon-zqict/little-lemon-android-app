package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID

class ValidateUserSessionUseCaseTest {
    private lateinit var useCase: ValidateUserSessionUseCase
    private lateinit var repository: AuthRepository

    @Before
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
    fun givenExpiredAccessTokenAndValidRefreshToken_useCaseReturns_success() = runTest {
        // Arrange
        val expiredToken = SessionToken(
            accessToken = UUID.randomUUID().toString(),
            accessTokenExpiry = LocalDateTime.now().minusDays(2),
            refreshToken = UUID.randomUUID().toString(),
            refreshTokenExpiry = LocalDateTime.now().plusDays(4)
        )
        val newToken = SessionToken(
            accessToken = UUID.randomUUID().toString(),
            accessTokenExpiry = LocalDateTime.now().plusDays(2),
            refreshToken = UUID.randomUUID().toString(),
            refreshTokenExpiry = LocalDateTime.now().plusDays(4)
        )
        coEvery { repository.getUserSession() } returns Resource.Success(expiredToken)
        coEvery { repository.refreshToken(expiredToken) } returns Resource.Success()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Success)
        val data = (result as Resource.Success).data
        assertEquals(newToken, data)
    }

    @Test
    fun givenExpiredAccessTokenAndExpiredRefreshToken_useCaseReturns_failure() = runTest {
        // Arrange
        val token = SessionToken(
            accessToken = UUID.randomUUID().toString(),
            accessTokenExpiry = LocalDateTime.now().minusDays(2),
            refreshToken = UUID.randomUUID().toString(),
            refreshTokenExpiry = LocalDateTime.now().minusDays(4)
        )
        coEvery { repository.getUserSession() } returns Resource.Success(token)
        coEvery { repository.refreshToken(token) } returns Resource.Failure()
        coEvery { repository.validateAccessToken() } returns Resource.Failure()
        coEvery { repository.validateRefreshToken() } returns Resource.Failure()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Failure)
    }

    @Test
    fun givenInvalidAccessToken_useCaseReturns_failure() = runTest {
        // Arrange
        val token = SessionToken(
            accessToken = UUID.randomUUID().toString(),
            accessTokenExpiry = LocalDateTime.now().minusDays(2),
            refreshToken = UUID.randomUUID().toString(),
            refreshTokenExpiry = LocalDateTime.now().minusDays(4)
        )
        coEvery { repository.getUserSession() } returns Resource.Success(token)
        coEvery { repository.refreshToken(token) } returns Resource.Failure()
        coEvery { repository.validateAccessToken() } returns Resource.Failure()
        coEvery { repository.validateRefreshToken() } returns Resource.Success()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Failure)
    }

    @Test
    fun givenInvalidRefreshToken_useCaseReturn_failure() = runTest {
        // Arrange
        val token = SessionToken(
            accessToken = UUID.randomUUID().toString(),
            accessTokenExpiry = LocalDateTime.now().minusDays(2),
            refreshToken = UUID.randomUUID().toString(),
            refreshTokenExpiry = LocalDateTime.now().minusDays(4)
        )
        coEvery { repository.getUserSession() } returns Resource.Success(token)
        coEvery { repository.refreshToken(token) } returns Resource.Failure()
        coEvery { repository.validateAccessToken() } returns Resource.Failure()
        coEvery { repository.validateRefreshToken() } returns Resource.Failure()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Failure)
    }

    @Test
    fun givenNoUserSession_useCaseReturn_failure() = runTest {
        // Arrange
        coEvery { repository.getUserSession() } returns Resource.Success(null)
        coEvery { repository.refreshToken(null) } returns Resource.Failure()
        coEvery { repository.validateAccessToken() } returns Resource.Failure()
        coEvery { repository.validateRefreshToken() } returns Resource.Failure()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Resource.Failure)
    }
}