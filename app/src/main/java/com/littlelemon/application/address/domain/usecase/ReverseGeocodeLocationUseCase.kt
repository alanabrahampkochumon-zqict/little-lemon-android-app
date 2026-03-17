package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.utils.Resource

class ReverseGeocodeLocationUseCase(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(location: LocalLocation): Resource<GeocodedAddress> =
        repository.reverseGeocodeLocation(location)
}