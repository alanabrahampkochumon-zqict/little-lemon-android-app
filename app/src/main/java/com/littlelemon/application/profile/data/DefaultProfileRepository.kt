package com.littlelemon.application.profile.data

import com.littlelemon.application.profile.data.mappers.toUserProfile
import com.littlelemon.application.profile.data.remote.SupabaseProfileRemoteDataSource
import com.littlelemon.application.profile.domain.ProfileRepository
import com.littlelemon.application.profile.domain.data.UserProfile

class DefaultProfileRepository(private val supabaseProfileRemoteDataSource: SupabaseProfileRemoteDataSource) :
    ProfileRepository {
    override suspend fun getUserProfile(): UserProfile {
        return supabaseProfileRemoteDataSource.getCurrentUserInfo().toUserProfile()
    }
}