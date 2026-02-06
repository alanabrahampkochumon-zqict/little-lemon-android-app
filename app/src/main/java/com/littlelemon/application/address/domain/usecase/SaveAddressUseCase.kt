package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.core.domain.utils.Resource

class SaveAddressUseCase(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(address: LocalAddress): Resource<Unit> =
        repository.saveAddress(address)
}