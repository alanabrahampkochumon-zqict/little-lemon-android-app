package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.core.domain.utils.Resource

class GetAddressUseCase(
    private val repository: AddressRepository
) {

    suspend operator fun invoke(): Resource<List<LocalAddress>> = repository.getAddress()
}