package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.auth.domain.models.User
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetUserSessionUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: GetUserSessionUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk<AuthRepository>()
        useCase = GetUserSessionUseCase(repository)
    }

    @Test
    fun onUseCaseInvocation_withRepositorySuccess_returnsSuccessWithUserSession() = runTest {
        // Given repository returns success with user session
        val session = SessionToken(
            refreshToken = "refresh_token",
            accessToken = "access_token",
            user = User(
                firstName = "first_name",
                lastName = "last_name",
                email = "user@email.com",
            ),
            tokenExpiry = LocalDateTime(
                year = 2026, month = 2, day = 25, hour = 12, minute = 7, second = 3, nanosecond = 16
            )
        )

        coEvery { repository.getUserSession() } returns Resource.Success(data = session)

        // When use case is invoked
        val result = useCase()

        // Then, we get a success resource with user session
        assertTrue(result is Resource.Success)
        assertEquals(session, result.data)
    }

    @Test
    fun onUseCaseInvocation_withRepositoryFailure_returnsFailure() = runTest {
        // Given a repository returns failure
        val errorMessage = "user not found"
        coEvery { repository.getUserSession() } returns Resource.Failure(errorMessage = errorMessage)

        // When use case is invoked
        val result = useCase()

        // Then, we get an error resource
        assertTrue(result is Resource.Failure)
        assertEquals(errorMessage, result.errorMessage)
    }
}