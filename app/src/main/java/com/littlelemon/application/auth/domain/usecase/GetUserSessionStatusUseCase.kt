package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.UserSessionStatus
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetUserSessionStatusUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<Resource<UserSessionStatus>> =
        repository.getUserSessionStatus()
}