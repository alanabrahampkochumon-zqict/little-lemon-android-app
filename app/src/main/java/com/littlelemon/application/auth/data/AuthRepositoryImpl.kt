package com.littlelemon.application.auth.data

import com.littlelemon.application.auth.data.mappers.toSessionToken
import com.littlelemon.application.auth.data.remote.AuthRemoteDataSource
import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.auth.domain.models.UserSessionStatus
import com.littlelemon.application.core.domain.utils.Error
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.domain.utils.toNetworkError
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

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
            currentCoroutineContext().ensureActive()
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
            currentCoroutineContext().ensureActive()
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
            currentCoroutineContext().ensureActive()
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
            currentCoroutineContext().ensureActive()
            Resource.Failure(error = Error.NetworkError.Unknown(), errorMessage = e.message)
        }
    }


    override suspend fun getUserSession(): Resource<SessionToken?> {
        return try {
            val session = remoteDataSource.getCurrentSession()
            Resource.Success(session?.toSessionToken())
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Resource.Failure(error = Error.SessionError.InvalidSession, errorMessage = e.message)
        }
    }

    override fun getUserSessionStatus(): Flow<Resource<UserSessionStatus>> {
        return remoteDataSource.getSessionStatus()
            .map { sessionStatus ->
                return@map when (sessionStatus) {
                    is SessionStatus.Authenticated -> Resource.Success(UserSessionStatus.Authenticated)
                    SessionStatus.Initializing -> Resource.Success(UserSessionStatus.Initializing)
                    is SessionStatus.NotAuthenticated -> Resource.Success(UserSessionStatus.Unauthenticated)
                    is SessionStatus.RefreshFailure -> Resource.Success(UserSessionStatus.Unauthenticated)
                } as Resource<UserSessionStatus>
            }.catch {
                emit(
                    Resource.Failure(
                        errorMessage = it.message ?: "An unknown error occurred!"
                    )
                )
            }
    }
}