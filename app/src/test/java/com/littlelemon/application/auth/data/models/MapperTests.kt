package com.littlelemon.application.auth.data.models

import android.location.Location
import com.littlelemon.application.auth.data.Constants
import com.littlelemon.application.auth.domain.models.LocalLocation
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
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

    @Nested
    inner class ToLocalLocationTest() {
        val LATITUDE = 1.2345
        val LONGITUDE = 3.2354

        @Test
        fun onLocationMapped_localLocationInstanceReturned() {
            // Arrange
            val location = mockk<Location>()
            every { location.latitude } returns LATITUDE
            every { location.longitude } returns LONGITUDE

            // Act
            val localLocation = location.toLocalLocation()

            // Assert
            assertNotNull(localLocation)
            assertTrue(localLocation is LocalLocation)
            assertEquals(LATITUDE, localLocation.latitude)
            assertEquals(LONGITUDE, localLocation.longitude)
        }
    }

}