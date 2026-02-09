package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetAddressUseCase(
    private val repository: AddressRepository
) {

    operator fun invoke(): Flow<Resource<List<LocalAddress>>> = repository.getAddress()
}