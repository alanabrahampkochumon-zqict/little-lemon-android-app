package com.littlelemon.application.auth.data.mappers

import com.littlelemon.application.auth.AuthConstants
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.auth.domain.models.User
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

fun UserSession.toSessionToken(): SessionToken? {
    return SessionToken(
        refreshToken = refreshToken,
        accessToken = accessToken,
        tokenExpiry = expiresAt.toLocalDateTime(TimeZone.currentSystemDefault()),
        user = User(
            email = this.user?.email ?: return null,
            firstName = this.user?.userMetadata?.get(AuthConstants.FIRST_NAME_KEY)?.jsonPrimitive?.contentOrNull
                ?: "",
            lastName = this.user?.userMetadata?.get(AuthConstants.LAST_NAME_KEY)?.jsonPrimitive?.contentOrNull
                ?: ""
        )
    )
}

