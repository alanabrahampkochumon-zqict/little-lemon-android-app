package com.littlelemon.application.auth.domain

import app.cash.turbine.test
import com.littlelemon.application.auth.domain.models.UserSessionGenerator
import com.littlelemon.application.auth.domain.models.UserSessionStatus
import com.littlelemon.application.auth.domain.usecase.GetUserSessionStatusUseCase
import com.littlelemon.application.core.domain.SessionManager
import com.littlelemon.application.core.domain.model.SessionStatus
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

class SessionManagerImplTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var getSessionUseCase: GetUserSessionStatusUseCase

    @BeforeEach
    fun setUp() {
        getSessionUseCase = mockk()
        sessionManager = SessionManagerImpl(getSessionUseCase)
    }

    @Test
    fun getCurrentSessionStatus_useCaseFailure_returnsUnauthenticated() = runTest {
        // Given use case returns failure
        val errorMessage = "user doesn't exist"
        coEvery { getSessionUseCase.invoke() } returns flow { emit(Resource.Failure(errorMessage = errorMessage)) }

        // Then, then the session status returned is Unauthenticated
        sessionManager.getCurrentSessionStatus().test {
            assertIs<SessionStatus.Unauthenticated>(awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun getCurrentSessionStatus_useCaseSuccessWithNullData_returnsUnauthenticated() =
        runTest {
            // Given use case returns success but with null data
            coEvery { getSessionUseCase.invoke() } returns flow { emit(Resource.Success(null)) }

            // Then, then the session status returned is Unauthenticated
            sessionManager.getCurrentSessionStatus().test {
                assertIs<SessionStatus.Unauthenticated>(awaitItem())

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun getCurrentSessionStatus_useCaseSuccessButWithoutUserFirstName_returnsPartiallyAuthenticated() =
        runTest {
            // Given use case returns success but with partial user info
            val session = UserSessionGenerator.generateUserSession(false)
            coEvery { getSessionUseCase.invoke() } returns flow {
                emit(
                    Resource.Success(
                        UserSessionStatus.Authenticated(session)
                    )
                )
            }

            // Then, then the session status returned is PartiallyAuthenticated
            sessionManager.getCurrentSessionStatus().test {
                assertIs<SessionStatus.PartiallyAuthenticated>(awaitItem())

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun getCurrentSessionStatus_useCaseSuccessWithSessionInitializing_returnsSessionLoading() =
        runTest {
            // Given use case returns success user session initializing
            val session = UserSessionGenerator.generateUserSession(false)
            coEvery { getSessionUseCase.invoke() } returns flow {
                emit(
                    Resource.Success(
                        UserSessionStatus.Initializing
                    )
                )
            }

            // Then, then the session status returned is SessionLoading
            sessionManager.getCurrentSessionStatus().test {
                assertIs<SessionStatus.SessionLoading>(awaitItem())

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun getCurrentSessionStatus_useCaseSuccessWithFullDetails_returnsFullyAuthenticated() =
        runTest {
            // Given use case returns success but with partial user info
            val session = UserSessionGenerator.generateUserSession(true)
            coEvery { getSessionUseCase.invoke() } returns flow {
                emit(
                    Resource.Success(
                        UserSessionStatus.Authenticated(session)
                    )
                )
            }

            // Then, then the session status returned is FullyAuthenticated
            sessionManager.getCurrentSessionStatus().test {
                assertIs<SessionStatus.FullyAuthenticated>(awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }

}