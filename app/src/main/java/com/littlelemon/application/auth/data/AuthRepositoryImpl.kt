package com.littlelemon.application.auth.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.littlelemon.application.auth.data.local.AuthLocalDataSource
import com.littlelemon.application.auth.data.models.toLocalLocation
import com.littlelemon.application.auth.data.models.toSessionToken
import com.littlelemon.application.auth.data.remote.AuthRemoteDataSource
import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.LocalLocation
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.core.domain.utils.Error
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.domain.utils.toNetworkError
import io.github.jan.supabase.exceptions.RestException

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {
    override suspend fun sendVerificationCode(emailAddress: String): Resource<Unit> {
        return try {
            remoteDataSource.sendVerificationCode(emailAddress)
            Resource.Success()
        } catch (e: RestException) {
            Resource.Failure(error = e.statusCode.toNetworkError(), errorMessage = e.message)
        } catch (e: Exception) {
            Resource.Failure(error = Error.NetworkError.Unknown(), errorMessage = e.message)
        }
    }

    override suspend fun verifyVerificationCode(
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

    override suspend fun resendVerificationCode(emailAddress: String): Resource<Unit> {
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

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getLocation(): Resource<LocalLocation> {
        return try {
            val location = localDataSource.getLocation()
            Resource.Success(location.toLocalLocation())
        } catch (e: Exception) {
            //TODO
            Resource.Failure(errorMessage = "Failed to fetch location")
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