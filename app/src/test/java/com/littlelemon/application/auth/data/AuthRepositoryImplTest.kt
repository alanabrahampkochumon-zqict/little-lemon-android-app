package com.littlelemon.application.auth.data

import app.cash.turbine.test
import com.littlelemon.application.auth.data.mappers.toSessionToken
import com.littlelemon.application.auth.data.remote.AuthRemoteDataSource
import com.littlelemon.application.auth.domain.models.UserSessionStatus
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.github.jan.supabase.auth.status.RefreshFailureCause
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertIs

@ExtendWith(StandardTestDispatcherRule::class)
class AuthRepositoryImplTest {

    private companion object {
        const val EMAIL_ADDRESS = "test@email.com"
        const val VERIFICATION_CODE = "316316"

        const val FIRST_NAME = "first name"
        const val LAST_NAME = "last name"

        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val EXPIRES_IN = 3600L

    }

    private val user = UserInfo(
        aud = "",
        email = EMAIL_ADDRESS,
        id = "101",
        userMetadata = buildJsonObject {
            put(Constants.FIRST_NAME_KEY, JsonPrimitive(FIRST_NAME))
            put(Constants.LAST_NAME_KEY, JsonPrimitive(LAST_NAME))
        },
    )

    private val userSession = UserSession(
        accessToken = ACCESS_TOKEN,
        refreshToken = REFRESH_TOKEN,
        expiresIn = EXPIRES_IN,
        tokenType = "bearer",
        user = user
    )
    private val remoteDataSource = mockk<AuthRemoteDataSource>()

    private val repository = AuthRepositoryImpl(
        remoteDataSource
    )

    @Test
    fun sendVerificationCode_remoteSuccess_returnsResourceSuccess() = runTest {
        // Arrange
        coEvery { remoteDataSource.sendVerificationCode(EMAIL_ADDRESS) } returns Unit

        // Act
        val result = repository.sendOTP(EMAIL_ADDRESS)

        // Assert
        assertTrue(result is Resource.Success)

        // Verify Remote Function is called
        coVerify(exactly = 1) { remoteDataSource.sendVerificationCode(EMAIL_ADDRESS) }
    }

    @Test
    fun sendVerificationCode_remoteFailure_returnResourceError() = runTest {
        // Arrange
        val expectedError = "Network Error"
        coEvery { remoteDataSource.sendVerificationCode(EMAIL_ADDRESS) } throws Exception(
            expectedError
        )

        // Act
        val result = repository.sendOTP(EMAIL_ADDRESS)

        // Assert
        assertTrue(result is Resource.Failure)
        result as Resource.Failure
        assertEquals(expectedError, result.errorMessage)
    }

    @Test
    fun verifyVerificationCode_remoteSuccess_returnsSuccessResource() = runTest {
        // Arrange
        coEvery {
            remoteDataSource.verifyVerificationCode(
                EMAIL_ADDRESS,
                VERIFICATION_CODE
            )
        } returns Unit

        // Act
        val result = repository.verifyOTP(EMAIL_ADDRESS, VERIFICATION_CODE)

        // Assert
        assertTrue(result is Resource.Success)

        // Verify Remote Function is called
        coVerify(exactly = 1) {
            remoteDataSource.verifyVerificationCode(
                EMAIL_ADDRESS,
                VERIFICATION_CODE
            )
        }
    }


    @Test
    fun verifyVerificationCode_remoteFailure_returnResourceError() = runTest {
        // Arrange
        val expectedError = "Network Error"
        coEvery {
            remoteDataSource.verifyVerificationCode(
                EMAIL_ADDRESS,
                VERIFICATION_CODE
            )
        } throws Exception(expectedError)

        // Act
        val result = repository.verifyOTP(EMAIL_ADDRESS, VERIFICATION_CODE)

        // Assert
        assertTrue(result is Resource.Failure)
        result as Resource.Failure
        assertEquals(expectedError, result.errorMessage)
    }

    @Test
    fun resendVerificationCode_remoteSuccess_returnsSuccessResource() = runTest {
        // Arrange
        coEvery { remoteDataSource.resendVerificationCode(EMAIL_ADDRESS) } returns Unit

        // Act
        val result = repository.resendOTP(EMAIL_ADDRESS)

        // Assert
        assertTrue(result is Resource.Success)

        // Verify Remote Function is called
        coVerify(exactly = 1) { remoteDataSource.resendVerificationCode(EMAIL_ADDRESS) }
    }


    @Test
    fun resendVerificationCode_remoteFailure_returnResourceError() = runTest {
        // Arrange
        val expectedError = "Network Error"
        coEvery { remoteDataSource.resendVerificationCode(EMAIL_ADDRESS) } throws Exception(
            expectedError
        )

        // Act
        val result = repository.resendOTP(EMAIL_ADDRESS)

        // Assert
        assertTrue(result is Resource.Failure)
        result as Resource.Failure
        assertEquals(expectedError, result.errorMessage)
    }

    @Test
    fun saveUserInformation_remoteSuccess_returnsSuccessResource() = runTest {
        // Arrange
        coEvery { remoteDataSource.savePersonalInformation(FIRST_NAME, LAST_NAME) } returns Unit

        // Act
        val result = repository.saveUserInformation(FIRST_NAME, LAST_NAME)

        // Assert
        assertTrue(result is Resource.Success)

        // Verify Remote Function is called
        coVerify(exactly = 1) { remoteDataSource.savePersonalInformation(FIRST_NAME, LAST_NAME) }
    }


    @Test
    fun saveUserInformation_remoteFailure_returnResourceError() = runTest {
        // Arrange
        val expectedError = "Network Error"
        coEvery {
            remoteDataSource.savePersonalInformation(
                FIRST_NAME,
                LAST_NAME
            )
        } throws Exception(expectedError)

        // Act
        val result = repository.saveUserInformation(FIRST_NAME, LAST_NAME)

        // Assert
        assertTrue(result is Resource.Failure)
        result as Resource.Failure
        assertEquals(expectedError, result.errorMessage)
    }

    @Test
    fun getSession_userHasValidSession_returnsResourceSuccessSession() = runTest {
        // Arrange
        coEvery { remoteDataSource.getCurrentSession() } returns userSession

        // Act
        val result = repository.getUserSession()

        // Assert
        assertTrue(result is Resource.Success)
        result as Resource.Success
        val data = result.data

        assertNotNull(data)
        assertEquals(ACCESS_TOKEN, data.accessToken)
        assertEquals(REFRESH_TOKEN, data.refreshToken)
        assertEquals(FIRST_NAME, data.user.firstName)
        assertEquals(LAST_NAME, data.user.lastName)
        assertEquals(EMAIL_ADDRESS, data.user.email)


        // Verify Remote Function is called
        coVerify(exactly = 1) { remoteDataSource.getCurrentSession() }
    }


    @Test
    fun getSession_userHasNoValidSession_returnsResourceSuccessWithNull() = runTest {
        // Arrange
        coEvery { remoteDataSource.getCurrentSession() } returns null

        // Act
        val result = repository.getUserSession()

        // Assert
        assertTrue(result is Resource.Success)
        result as Resource.Success
        assertNull(result.data)
    }

    @Test
    fun getSession_remoteError_returnsErrorResource() = runTest {
        // Arrange
        val expectedError = "Network Error"
        coEvery { remoteDataSource.getCurrentSession() } throws Exception(expectedError)

        // Act
        val result = repository.getUserSession()

        // Assert
        assertTrue(result is Resource.Failure)
        result as Resource.Failure
        assertEquals(expectedError, result.errorMessage)
    }

    @Nested
    inner class GetUserSessionStatusTest {


        @Test
        fun getUserSessionStatus_remoteUserStatusIsInitializing_returnsSuccessWithStatusInitializing() =
            runTest {
                // Given remoteDatasource emits session status of initializing
                coEvery { remoteDataSource.getSessionStatus() } returns flow { emit(SessionStatus.Initializing) }

                // When user status is called
                repository.getUserSessionStatus().test {
                    // Then, the collected value is Initializing wrapped with resource success
                    val result = awaitItem()
                    assertIs<Resource.Success<UserSessionStatus>>(result)
                    assertTrue { result.data is UserSessionStatus.Initializing }

                    cancelAndConsumeRemainingEvents()
                }
            }


        @Test
        fun getUserSessionStatus_remoteUserStatusIsAuthenticated_returnsSuccessWithStatusAuthenticated() =
            runTest {
                // Given remoteDatasource emits session status of authenticated
                val userSession = UserSession(
                    "access_token",
                    "refresh_token",
                    null,
                    null,
                    60000L,
                    "refresh",
                    null
                )
                coEvery { remoteDataSource.getSessionStatus() } returns flow {
                    emit(
                        SessionStatus.Authenticated(
                            userSession
                        )
                    )
                }

                // When user status is called
                repository.getUserSessionStatus().test {
                    // Then, the collected value is Authenticated wrapped with resource success
                    val result = awaitItem()
                    assertIs<Resource.Success<UserSessionStatus>>(result)
                    assertIs<UserSessionStatus.Authenticated>(result.data)
                    // And it contains the passed in session
                    assertEquals(userSession.toSessionToken(), result.data.userSession)
                    cancelAndConsumeRemainingEvents()
                }
            }


        @Test
        fun getUserSessionStatus_remoteUserStatusIsNotAuthenticated_returnsSuccessWithStatusUnauthenticated() =
            runTest {
                // Given remoteDatasource emits session status of not authenticated
                coEvery { remoteDataSource.getSessionStatus() } returns flow {
                    emit(SessionStatus.NotAuthenticated())
                }

                // When user status is called
                repository.getUserSessionStatus().test {
                    // Then, the collected value is Unauthenticated wrapped with resource success
                    val result = awaitItem()
                    assertIs<Resource.Success<UserSessionStatus>>(result)
                    assertTrue { result.data is UserSessionStatus.Unauthenticated }

                    cancelAndConsumeRemainingEvents()
                }
            }


        @Test
        fun getUserSessionStatus_remoteUserStatusIsRefreshFailure_returnsSuccessWithStatusUnauthenticated() =
            runTest {
                // Given remoteDatasource emits session status of refresh failure
                coEvery { remoteDataSource.getSessionStatus() } returns flow {
                    emit(SessionStatus.RefreshFailure(RefreshFailureCause.NetworkError(Exception())))
                }

                // When user status is called
                repository.getUserSessionStatus().test {
                    // Then, the collected value is Unauthenticated wrapped with resource success
                    val result = awaitItem()
                    assertIs<Resource.Success<UserSessionStatus>>(result)
                    assertTrue { result.data is UserSessionStatus.Unauthenticated }

                    cancelAndConsumeRemainingEvents()
                }
            }


        @Test
        fun getUserSessionStatus_remoteFailure_returnsFailure() =
            runTest {

                // Given remoteDatasource emits session status of refresh failure
                coEvery { remoteDataSource.getSessionStatus() } returns flow {
                    throw RuntimeException()
                }

                // When user status is called
                repository.getUserSessionStatus().test {
                    // Then, the collected value is resource failure
                    val result = awaitItem()
                    assertIs<Resource.Failure<UserSessionStatus>>(result)

                    cancelAndConsumeRemainingEvents()
                }

            }

    }
}