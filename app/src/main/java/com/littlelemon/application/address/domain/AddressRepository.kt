package com.littlelemon.application.address.domain

import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.utils.Resource

interface AddressRepository {
    suspend fun getLocation(): Resource<LocalLocation>
}