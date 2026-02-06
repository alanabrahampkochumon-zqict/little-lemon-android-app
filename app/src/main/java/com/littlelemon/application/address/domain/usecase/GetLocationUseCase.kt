package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.DefaultUseCase
import com.littlelemon.application.core.domain.utils.Resource

class GetLocationUseCase(private val repository: AddressRepository) :
    DefaultUseCase<Resource<LocalLocation>> {
    override suspend fun invoke(): Resource<LocalLocation> = repository.getLocation()
}