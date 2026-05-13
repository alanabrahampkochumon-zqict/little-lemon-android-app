package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalAddress

class RemoveAddressUseCase(private val repository: AddressRepository) {
    suspend operator fun invoke(address: LocalAddress) = repository.removeAddress(address)
}