package com.littlelemon.application.auth.data

import com.littlelemon.application.auth.data.mappers.toSessionToken
import com.littlelemon.application.auth.data.remote.AuthRemoteDataSource
import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.core.domain.utils.Error
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.domain.utils.toNetworkError
import io.github.jan.supabase.exceptions.RestException

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun sendOTP(emailAddress: String): Resource<Unit> {
        return try {
            remoteDataSource.sendVerificationCode(emailAddress)
            Resource.Success()
        } catch (e: RestException) {
            Resource.Failure(error = e.statusCode.toNetworkError(), errorMessage = e.message)
        } catch (e: Exception) {
            Resource.Failure(error = Error.NetworkError.Unknown(), errorMessage = e.message)
        }
    }

    override suspend fun verifyOTP(
        emailAddress: String,
        verificationCode: String
    ): Resource<Unit> {
        return try {
            remoteDataSource.verifyVerificationCode(emailAddress, verificationCode)
            Resource.Success()
        } catch (e: RestException) {
            Resource.Failure(error = e.statusCode.toNetworkError(), errorMessage = e.message)
        } catch (e: Exception) {
            Resource.Failure(error = Error.NetworkError.Unknown(), errorMessage = e.message)
        }
    }

    override suspend fun resendOTP(emailAddress: String): Resource<Unit> {
        return try {
            remoteDataSource.resendVerificationCode(emailAddress)
            Resource.Success()
        } catch (e: RestException) {
            Resource.Failure(error = e.statusCode.toNetworkError(), errorMessage = e.message)
        } catch (e: Exception) {
            Resource.Failure(error = Error.NetworkError.Unknown(), errorMessage = e.message)
        }
    }

    override suspend fun saveUserInformation(
        firstName: String,
        lastName: String
    ): Resource<Unit> {
        return try {
            remoteDataSource.savePersonalInformation(firstName, lastName)
            Resource.Success()
        } catch (e: RestException) {
            Resource.Failure(error = e.statusCode.toNetworkError(), errorMessage = e.message)
        } catch (e: Exception) {
            Resource.Failure(error = Error.NetworkError.Unknown(), errorMessage = e.message)
        }
    }


    override suspend fun getUserSession(): Resource<SessionToken?> {
        return try {
            val session = remoteDataSource.getCurrentSession()
            Resource.Success(session?.toSessionToken())
        } catch (e: Exception) {
            Resource.Failure(error = Error.SessionError.InvalidSession, errorMessage = e.message)
        }
    }
}