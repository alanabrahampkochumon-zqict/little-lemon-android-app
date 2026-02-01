package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.core.domain.DefaultUseCase
import com.littlelemon.application.core.domain.utils.Error
import com.littlelemon.application.core.domain.utils.Resource

class ValidateUserSessionUseCase(private val repository: AuthRepository) :
    DefaultUseCase<Resource<SessionToken>> {
    override suspend fun invoke(): Resource<SessionToken> {
        val result = repository.getUserSession()
        return if (result is Resource.Success) {
            if (result.data != null) {
                Resource.Success(result.data)
            } else {
                Resource.Failure(
                    errorMessage = "Session not found",
                    error = Error.SessionError.SessionTokenNotFound
                )
            }
        } else {
            Resource.Failure(
                errorMessage = "Unknown error occurred",
                error = Error.SessionError.Unknown
            )
        }
    }
}