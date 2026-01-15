package com.littlelemon.application.auth.data.models

import com.littlelemon.application.auth.data.Constants
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MapperTests {

    @Nested
    inner class ToSessionTokenTest() {

            val EMAIL_ADDRESS = "test@email.com"

            val FIRST_NAME = "little lemon"
            val LAST_NAME = "the best"

            val ACCESS_TOKEN = "access_token"
            val REFRESH_TOKEN = "refresh_token"
            val EXPIRES_IN = 3600L


        private val user = UserInfo(
            aud = "",
            email = EMAIL_ADDRESS,
            id = "101",
            userMetadata = buildJsonObject{
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

        @Test
        fun validSessionValidEmail_returns_UserSessionObject() {
            // Arrange

            // Act
            val session = userSession.toSessionToken()

            // Assert
            assertNotNull(session)
            assertEquals(userSession.accessToken, session?.accessToken)
            assertEquals(userSession.refreshToken, session?.refreshToken)
            assertEquals(userSession.user?.email, session?.user?.email)
            assertEquals(FIRST_NAME, session?.user?.firstName)
            assertEquals(LAST_NAME, session?.user?.lastName)
        }

        @Test
        fun validSessionInvalidEmail_returnsNull() {
            // Arrange
            val invalidSession = userSession.copy(user = null)

            // Act
            val session = invalidSession.toSessionToken()

            // Assert
            assertNull(session)
        }

        @Test
        fun nullSession_returnsNull() {
            // Arrange
            val invalidSession: UserSession? = null

            // Act
            val session = invalidSession?.toSessionToken()

            // Assert
            assertNull(session)
        }
    }

}