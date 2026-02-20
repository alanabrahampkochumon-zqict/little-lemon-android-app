package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.SessionToken
import com.littlelemon.application.core.domain.utils.Resource

class GetUserSessionUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Resource<SessionToken?> = repository.getUserSession()
}