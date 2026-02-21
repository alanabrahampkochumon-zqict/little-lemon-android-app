package com.littlelemon.application.auth.domain.usecase

import app.cash.turbine.test
import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.UserSessionGenerator
import com.littlelemon.application.auth.domain.models.UserSessionStatus
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetUserSessionUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: GetUserSessionStatusUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk<AuthRepository>()
        useCase = GetUserSessionStatusUseCase(repository)
    }

    @Test
    fun onUseCaseInvocation_withRepositorySuccess_returnsSuccessWithUserSession() = runTest {
        // Given repository returns success with user session
        val session = UserSessionGenerator.generateUserSession(true)

        coEvery { repository.getUserSessionStatus() } returns flow {
            emit(
                Resource.Success(
                    data = UserSessionStatus.Authenticated(
                        session
                    )
                )
            )
        }

        // When use case is invoked
        val result = useCase()

        // Then, we get a success resource with user session
        result.test {
            val resource = awaitItem()
            assertIs<Resource.Success<UserSessionStatus>>(resource)
            assertIs<UserSessionStatus.Authenticated>(resource.data)
            assertEquals(session, resource.data.userSession)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun onUseCaseInvocation_withRepositoryFailure_returnsFailure() = runTest {
        // Given a repository returns failure
        val errorMessage = "user not found"
        coEvery { repository.getUserSessionStatus() } returns flow {
            emit(
                Resource.Failure(
                    errorMessage = errorMessage
                )
            )
        }

        // When use case is invoked
        val result = useCase()

        // Then, we get an error resource
        result.test {
            val resource = awaitItem()
            assertIs<Resource.Failure<UserSessionStatus>>(resource)
            assertEquals(errorMessage, resource.errorMessage)

            cancelAndConsumeRemainingEvents()
        }
    }
}