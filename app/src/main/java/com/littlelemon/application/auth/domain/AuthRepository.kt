package com.littlelemon.application.auth.domain

import com.littlelemon.application.auth.domain.models.Location
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.auth.domain.models.User
import com.littlelemon.application.core.domain.utils.Resource

interface AuthRepository {

    suspend fun sendVerificationCode(email: String): Resource<Unit>

    suspend fun verifyVerificationCode(otp: String): Resource<User>

    suspend fun saveUserInformation(firstName: String, lastName: String = ""): Resource<User>

    suspend fun getLocationPermission(): Resource<Unit>

    suspend fun getLocation(): Resource<Location>

    suspend fun getUserSession(): Resource<SessionToken?>

    suspend fun validateAccessToken(): Resource<Unit>

    suspend fun validateRefreshToken(): Resource<Unit>

    suspend fun refreshToken(session: SessionToken?): Resource<Unit>
}