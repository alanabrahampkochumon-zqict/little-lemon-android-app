package com.littlelemon.application.profile.data.mappers

import com.littlelemon.application.auth.AuthConstants
import com.littlelemon.application.profile.domain.data.UserProfile
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.jsonPrimitive

// Not tested due coupling with Supabase
fun UserInfo.toUserProfile(): UserProfile {

    val firstName = userMetadata?.get(AuthConstants.FIRST_NAME_KEY)
        ?.jsonPrimitive?.content ?: "Little Lemon"

    val lastName = userMetadata?.get(AuthConstants.LAST_NAME_KEY)
        ?.jsonPrimitive?.content ?: "User"

    val name = "$firstName $lastName"
    val email = email ?: "Unauthenticated"

    return UserProfile(
        name = name, email = email
    )
}