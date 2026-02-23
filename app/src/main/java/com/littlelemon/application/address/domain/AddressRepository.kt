package com.littlelemon.application.address.domain

import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun getLocation(): Resource<LocalLocation>

    suspend fun saveAddress(address: LocalAddress): Resource<Unit>

    fun getAddress(): Flow<Resource<List<LocalAddress>>>

    suspend fun getAddressCount(): Long
}