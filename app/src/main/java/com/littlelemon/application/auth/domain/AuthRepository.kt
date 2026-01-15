package com.littlelemon.application.auth.domain

import com.littlelemon.application.auth.domain.models.Location
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.auth.domain.models.User
import com.littlelemon.application.core.domain.utils.Resource

interface AuthRepository {

    suspend fun sendVerificationCode(emailAddress: String): Resource<Unit>

    suspend fun verifyVerificationCode(emailAddress: String, verificationCode: String): Resource<Unit>

    suspend fun resendVerificationCode(emailAddress: String): Resource<Unit>

    suspend fun saveUserInformation(firstName: String, lastName: String = ""): Resource<Unit>

    suspend fun getLocationPermission(): Resource<Unit>

    suspend fun getLocation(): Resource<Location>

    suspend fun getUserSession(): Resource<SessionToken?>
}