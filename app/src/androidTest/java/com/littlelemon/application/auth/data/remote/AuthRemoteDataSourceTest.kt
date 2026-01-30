package com.littlelemon.application.auth.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.MemorySessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuthRemoteDataSourceTest {
    private companion object {
        const val EMAIL_ADDRESS = "test@email.com"
        const val VERIFICATION_CODE = "123456"
        const val FIRST_NAME = "first name"
        const val LAST_NAME = "last name"
        const val ACCESS_TOKEN = "access_token"
        const val NEW_ACCESS_TOKEN = "new_access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val NEW_REFRESH_TOKEN = "new_refresh_token"
        const val EXPIRES_IN = 2000L

    }

    @Test
    fun onVerificationCodeSend_success_noExceptionThrown() = runTest {
        // 1. Arrange: Setup Success Engine locally
        val datasource = createDataSource(
            content = """{"message": "success"}""",
            statusCode = HttpStatusCode.OK
        )

        // 2. Act: Should not throw
        datasource.sendVerificationCode(EMAIL_ADDRESS)
    }

    @Test
    fun onVerificationCodeSend_error_exceptionThrown() = runTest {
        // 1. Arrange: Setup Error Engine
        val datasource = createDataSource(
            content = """{"error": "Too many requests"}""",
            statusCode = HttpStatusCode.TooManyRequests
        )

        // 2. Act
        val exception = assertFailsWith<RestException> {
            datasource.sendVerificationCode(EMAIL_ADDRESS)
        }

        // 3. Assert: Verify error code
        assertEquals(HttpStatusCode.TooManyRequests.value, exception.statusCode)
    }

    @Test
    fun verifyVerificationCode_success_noExceptionThrown() = runTest {
        // 1. Arrange: Set up the success engine
        val dataSource = createDataSource(
            content = """{"access_token": "access_token", "refresh_token": "refresh_token", "expires_in": "1234", "token_type": "Access"}""",
            statusCode = HttpStatusCode.OK
        )

        // 2. Act & Assert: Shouldn't throw an exception
        dataSource.verifyVerificationCode(EMAIL_ADDRESS, VERIFICATION_CODE)
    }

    @Test
    fun verifyVerificationCode_failure_exceptionThrown() = runTest {
        // 1. Arrange: Set up the failure engine
        val dataSource = createDataSource(
            content = """{"error": "Invalid OTP"}""",
            statusCode = HttpStatusCode.BadRequest
        )

        // 2. Act
        val exception = assertFailsWith<RestException> {
            dataSource.verifyVerificationCode(
                EMAIL_ADDRESS,
                VERIFICATION_CODE
            )
        }

        assertEquals(HttpStatusCode.BadRequest.value, exception.statusCode)
    }

    @Test
    fun savePersonalInformation_success_noExceptionThrown() = runTest {
        // 1. Arrange: Setup the success engine
        val validUserResponse = """
        {
          "id": "11111111-2222-3333-4444-555555555555",
          "aud": "authenticated",
          "role": "authenticated",
          "email": "$EMAIL_ADDRESS",
          "email_confirmed_at": "2023-01-01T00:00:00Z",
          "phone": "",
          "confirmed_at": "2023-01-01T00:00:00Z",
          "last_sign_in_at": "2023-01-01T00:00:00Z",
          "app_metadata": {
            "provider": "email",
            "providers": ["email"]
          },
          "user_metadata": {
            "first_name": "$FIRST_NAME",
            "last_name": "$LAST_NAME"
          },
          "identities": [],
          "created_at": "2023-01-01T00:00:00Z",
          "updated_at": "2023-01-02T00:00:00Z"
        }
        """.trimIndent()

        val dataSource = createDataSource(
            content = validUserResponse,
            statusCode = HttpStatusCode.OK
        )

        // 2. Act & Assert: No exception is thrown
        dataSource.savePersonalInformation(FIRST_NAME, LAST_NAME)
    }

    @Test
    fun savePersonalInformation_failure_exceptionThrown() = runTest {
        //1. Arrange: Setup the failure engine
        val dataSource = createDataSource(
            content = """ {"error": "Unknown Error"} """,
            statusCode = HttpStatusCode.BadRequest
        )

        // 2. Act
        val exception = assertFailsWith<RestException> {
            dataSource.savePersonalInformation(
                FIRST_NAME,
                LAST_NAME
            )
        }

        // 3. Assert
        assertEquals(HttpStatusCode.BadRequest.value, exception.statusCode)
    }

    @Test
    fun resendVerificationCode_success_noExceptionThrown() = runTest {
        // 1. Arrange
        val response = """
            {
                "data": {
                    "session": null,
                    "user": null
                }
            }
        """.trimIndent()

        val dataSource = createDataSource(
            content = response,
            statusCode = HttpStatusCode.OK
        )

        // 2. Act & Assert
        dataSource.resendVerificationCode(EMAIL_ADDRESS)
    }

    @Test
    fun resendVerificationCode_failure_exceptionThrown() = runTest {
        // 1. Arrange
        val dataSource = createDataSource(
            content = """ {"error": "failure" }""",
            statusCode = HttpStatusCode.BadRequest
        )

        // 2. Act
        val exception =
            assertFailsWith<RestException> { dataSource.resendVerificationCode(EMAIL_ADDRESS) }

        // 3. Assert
        assertEquals(HttpStatusCode.BadRequest.value, exception.statusCode)
    }

    @Test
    fun getCurrentSession_userHasValidSession_returnSession() = runTest {
        // 1. Arrange
        val client = createSupabaseClient("fake-url", "fake-key") {
            install(Auth) {
                sessionManager = MemorySessionManager()
            }
        }
        val createdSession = UserSession(
            accessToken = ACCESS_TOKEN,
            refreshToken = REFRESH_TOKEN,
            expiresIn = EXPIRES_IN,
            tokenType = "bearer"
        )
        client.auth.importSession(createdSession, autoRefresh = false)

        val dataSource = AuthRemoteDataSource(client)

        // 2. Act
        val returnedSession = dataSource.getCurrentSession()

        // 3. Assert
        assertNotNull(returnedSession)
        assertEquals(createdSession.accessToken, returnedSession.accessToken)
        assertEquals(createdSession.refreshToken, returnedSession.refreshToken)
        assertEquals(createdSession.expiresIn, returnedSession.expiresIn)
        assertEquals(createdSession.tokenType, returnedSession.tokenType)
    }

    @Test
    fun getCurrentSession_userHasNoValidSession_returnNull() = runTest {
        // 1. Arrange
        val client = createSupabaseClient("fake-url", "fake-key") {
            install(Auth) {
                sessionManager = MemorySessionManager()
            }
        }
        val dataSource = AuthRemoteDataSource(client)

        // 2. Act
        val session = dataSource.getCurrentSession()

        // 3. Assert
        assertNull(session)
    }

    @Test
    fun refreshSession_success_refreshCurrentToken() = runTest {
        val response = """
        {
          "access_token": "$NEW_ACCESS_TOKEN",
          "token_type": "bearer",
          "expires_in": 3600,
          "refresh_token": "$NEW_REFRESH_TOKEN",
          "user": {
            "id": "11111111-1111-1111-1111-111111111111",
            "aud": "authenticated",
            "role": "authenticated",
            "email": "$EMAIL_ADDRESS",
            "phone": "",
            "app_metadata": { "provider": "email", "providers": ["email"] },
            "user_metadata": {},
            "identities": [],
            "created_at": "2024-01-01T00:00:00Z",
            "updated_at": "2024-01-01T00:00:00Z",
            "is_anonymous": false
          }
        }
    """.trimIndent()
        // 1. Arrange
        val engine = MockEngine {
            respond(
                content = response,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }


        val client = createSupabaseClient("fake-url", "fake-key") {
            httpEngine = engine
            install(Auth) {
                sessionManager = MemorySessionManager()
            }
        }

        val createdSession = UserSession(
            accessToken = ACCESS_TOKEN,
            refreshToken = REFRESH_TOKEN,
            expiresIn = EXPIRES_IN,
            tokenType = "bearer"
        )
        client.auth.importSession(createdSession, autoRefresh = false)

        val dataSource = AuthRemoteDataSource(client)

        // 2. Act
        dataSource.refreshSession()
        val session = dataSource.getCurrentSession()

        assertNotNull(session)
        assertEquals(NEW_ACCESS_TOKEN, session.accessToken)
        assertEquals(NEW_REFRESH_TOKEN, session.refreshToken)
    }

    @Test
    fun refreshSession_failure_throwsException() = runTest {
        // 1. Arrange
        val engine = MockEngine {
            respond(
                content = """{"error": "invalid_grant", "data": { "session": null, "user": null } }""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }


        val client = createSupabaseClient("fake-url", "fake-key") {
            httpEngine = engine
            install(Auth) {
                sessionManager = MemorySessionManager()
            }
        }

        val createdSession = UserSession(
            accessToken = ACCESS_TOKEN,
            refreshToken = REFRESH_TOKEN,
            expiresIn = EXPIRES_IN,
            tokenType = "bearer"
        )
        client.auth.importSession(createdSession, autoRefresh = false)

        val dataSource = AuthRemoteDataSource(client)

        // 2. Act
        val exception = assertFailsWith<RestException> { dataSource.refreshSession() }

        // 3. Assert
        assertEquals(HttpStatusCode.BadRequest.value, exception.statusCode)
    }

    private fun createDataSource(
        content: String,
        statusCode: HttpStatusCode
    ): AuthRemoteDataSource {
        val engine = MockEngine {
            respond(
                content = content,
                status = statusCode,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = createSupabaseClient("fake-url", "fake-key") {
            httpEngine = engine
            install(Auth) {
                sessionManager = MemorySessionManager()
            }
        }

        return AuthRemoteDataSource(client)

    }
}