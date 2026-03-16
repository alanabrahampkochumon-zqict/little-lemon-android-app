package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.core.domain.utils.Resource

class GeocodeAddressUseCase(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(address: String): Resource<GeocodedAddress> =
        repository.geocodeAddress(address)
}