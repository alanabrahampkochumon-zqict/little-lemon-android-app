package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.LocalLocation
import com.littlelemon.application.core.domain.DefaultUseCase
import com.littlelemon.application.core.domain.utils.Resource

class GetLocationUseCase(private val repository: AuthRepository) :
    DefaultUseCase<Resource<LocalLocation>> {
    override suspend fun invoke(): Resource<LocalLocation> = repository.getLocation()
}