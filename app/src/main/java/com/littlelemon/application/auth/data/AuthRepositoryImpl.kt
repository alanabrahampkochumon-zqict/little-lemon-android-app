package com.littlelemon.application.auth.data

import com.littlelemon.application.auth.data.local.AuthLocalDataSource
import com.littlelemon.application.auth.data.remote.AuthRemoteDataSource
import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.Location
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.auth.domain.models.User
import com.littlelemon.application.core.domain.utils.Resource

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {
    override suspend fun sendVerificationCode(email: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyVerificationCode(otp: String): Resource<User> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserInformation(
        firstName: String,
        lastName: String
    ): Resource<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocationPermission(): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocation(): Resource<Location> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserSession(): Resource<SessionToken?> {
        TODO("Not yet implemented")
    }
}