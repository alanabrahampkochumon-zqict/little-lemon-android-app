package com.littlelemon.application.auth.domain

import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.auth.domain.models.UserSessionStatus
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun sendOTP(emailAddress: String): Resource<Unit>

    suspend fun verifyOTP(
        emailAddress: String,
        verificationCode: String
    ): Resource<Unit>

    suspend fun resendOTP(emailAddress: String): Resource<Unit>

    suspend fun saveUserInformation(firstName: String, lastName: String = ""): Resource<Unit>

    suspend fun getUserSession(): Resource<SessionToken?>

    fun getUserSessionStatus(): Flow<Resource<UserSessionStatus>>
}