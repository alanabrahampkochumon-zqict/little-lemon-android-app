package com.littlelemon.application.profile.domain

import com.littlelemon.application.profile.domain.data.UserProfile

interface ProfileRepository {

    suspend fun getUserProfile(): UserProfile
}