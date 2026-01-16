package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.core.domain.DefaultUseCase
import com.littlelemon.application.core.domain.utils.Resource

class GetLocationPermissionUseCase(private val repository: AuthRepository) :
    DefaultUseCase<Resource<Unit>> {

    override suspend fun invoke(): Resource<Unit> = TODO()

}