package com.littlelemon.application.auth.domain

import com.littlelemon.application.auth.domain.models.LocalLocation
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.core.domain.utils.Resource

interface AuthRepository {

    suspend fun sendVerificationCode(emailAddress: String): Resource<Unit>

    suspend fun verifyVerificationCode(
        emailAddress: String,
        verificationCode: String
    ): Resource<Unit>

    suspend fun resendVerificationCode(emailAddress: String): Resource<Unit>

    suspend fun saveUserInformation(firstName: String, lastName: String = ""): Resource<Unit>

    suspend fun getLocation(): Resource<LocalLocation>

    suspend fun getUserSession(): Resource<SessionToken?>
}