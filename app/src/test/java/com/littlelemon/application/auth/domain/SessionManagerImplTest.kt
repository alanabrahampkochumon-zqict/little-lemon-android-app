package com.littlelemon.application.auth.domain

import app.cash.turbine.test
import com.littlelemon.application.auth.domain.models.UserSessionGenerator
import com.littlelemon.application.auth.domain.usecase.GetUserSessionUseCase
import com.littlelemon.application.core.domain.SessionManager
import com.littlelemon.application.core.domain.model.SessionStatus
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

class SessionManagerImplTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var getSessionUseCase: GetUserSessionUseCase

    @BeforeEach
    fun setUp() {
        getSessionUseCase = mockk()
        sessionManager = SessionManagerImpl(getSessionUseCase)
    }

    @Test
    fun onGetCurrentSessionStatus_useCaseFailure_returnsUnauthenticated() = runTest {
        // Given use case returns failure
        val errorMessage = "user doesn't exist"
        coEvery { getSessionUseCase.invoke() } returns Resource.Failure(errorMessage = errorMessage)

        // Then, then the session status returned is Unauthenticated
        sessionManager.getCurrentSessionStatus().test {
            assertIs<SessionStatus.Unauthenticated>(awaitItem())

            cancelAndConsumeRemainingEvents()
        }
//        assertTrue(sessionManager.getCurrentSessionStatus() is SessionStatus.Unauthenticated)
    }

    @Test
    fun onGetCurrentSessionStatus_useCaseSuccessWithNullData_returnsUnauthenticated() =
        runTest {
            // Given use case returns success but with null data
            coEvery { getSessionUseCase.invoke() } returns Resource.Success(null)

            // Then, then the session status returned is Unauthenticated
            sessionManager.getCurrentSessionStatus().test {
                assertIs<SessionStatus.Unauthenticated>(awaitItem())

                cancelAndConsumeRemainingEvents()
            }
//            assertTrue(sessionManager.getCurrentSessionStatus() is SessionStatus.Unauthenticated)
        }

    @Test
    fun onGetCurrentSessionStatus_useCaseSuccessButWithoutUserFirstName_returnsPartiallyAuthenticated() =
        runTest {
            // Given use case returns success but with partial user info
            val session = UserSessionGenerator.generateUserSession(false)
            coEvery { getSessionUseCase.invoke() } returns Resource.Success(session)

            // Then, then the session status returned is PartiallyAuthenticated
            sessionManager.getCurrentSessionStatus().test {
                assertIs<SessionStatus.PartiallyAuthenticated>(awaitItem())

                cancelAndConsumeRemainingEvents()
            }
//            assertTrue(sessionManager.getCurrentSessionStatus() is SessionStatus.PartiallyAuthenticated)
        }

    @Test
    fun onGetCurrentSessionStatus_useCaseSuccessWithFullDetails_returnsFullyAuthenticated() =
        runTest {
            // Given use case returns success but with partial user info
            val session = UserSessionGenerator.generateUserSession(true)
            coEvery { getSessionUseCase.invoke() } returns Resource.Success(session)

            // Then, then the session status returned is FullyAuthenticated
            sessionManager.getCurrentSessionStatus().test {
                assertIs<SessionStatus.FullyAuthenticated>(awaitItem())
                cancelAndConsumeRemainingEvents()
            }
//            assertTrue(sessionManager.getCurrentSessionStatus() is SessionStatus.FullyAuthenticated)
        }

}