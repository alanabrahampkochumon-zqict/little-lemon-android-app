package com.littlelemon.application.profile.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo

class SupabaseProfileRemoteDataSource(
    private val client: SupabaseClient
) {

    suspend fun getCurrentUserInfo(): UserInfo {
        return client.auth.retrieveUserForCurrentSession(updateSession = true)
    }
}