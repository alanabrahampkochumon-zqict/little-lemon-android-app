package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.Location
import com.littlelemon.application.core.domain.DefaultUseCase
import com.littlelemon.application.core.domain.utils.Resource

class GetLocationUseCase(private val repository: AuthRepository) :
    DefaultUseCase<Resource<Location>> {
    override suspend fun invoke(): Resource<Location> = repository.getLocation()

}