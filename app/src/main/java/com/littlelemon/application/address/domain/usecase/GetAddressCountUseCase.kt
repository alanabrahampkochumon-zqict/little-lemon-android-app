package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository

class GetAddressCountUseCase(private val repository: AddressRepository) {

    suspend operator fun invoke(): Long = repository.getAddressCount()
}