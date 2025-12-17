package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.core.domain.DefaultUseCase
import com.littlelemon.application.core.domain.utils.Resource

class ValidateUserSessionUseCase(private val repository: AuthRepository) :
    DefaultUseCase<Resource<SessionToken>> {
    override suspend fun invoke(): Resource<SessionToken> {
        TODO("Not yet implemented")
    }
}