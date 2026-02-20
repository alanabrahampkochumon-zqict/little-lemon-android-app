package com.littlelemon.application.auth.domain.models

import io.github.serpro69.kfaker.faker
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object UserSessionGenerator {

    val faker = faker { }

    /**
     * Generates a faker user session.
     * Note: Session Expiry is year 3000, if you want to customize it use `object.copy` on the return object.
     * @param addUserDetails: Whether to add first and last name
     * @return SessionToken object.
     */
    @OptIn(ExperimentalUuidApi::class)
    fun generateUserSession(addUserDetails: Boolean = false): SessionToken {
        return SessionToken(
            refreshToken = Uuid.random().toString(),
            accessToken = Uuid.random().toString(),
            tokenExpiry = LocalDateTime(year = 3000, month = 12, day = 5, hour = 12, minute = 12),
            user = User(
                email = faker.internet.safeEmail(),
                firstName = if (addUserDetails) faker.name.firstName() else null,
                lastName = if (addUserDetails) faker.name.lastName() else null
            )
        )
    }
}