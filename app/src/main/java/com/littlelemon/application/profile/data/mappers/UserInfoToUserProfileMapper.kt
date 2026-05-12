package com.littlelemon.application.profile.data.mappers

import com.littlelemon.application.auth.AuthConstants
import com.littlelemon.application.profile.domain.data.UserProfile
import io.github.jan.supabase.auth.user.UserInfo

// Not tested due coupling with Supabase
fun UserInfo.toUserProfile(): UserProfile {
    return UserProfile(
        name = userMetadata?.get(AuthConstants.FIRST_NAME_KEY).toString() + userMetadata?.get(
            AuthConstants.LAST_NAME_KEY
        ).toString(), email = email ?: "Unauthenticated"
    )
}