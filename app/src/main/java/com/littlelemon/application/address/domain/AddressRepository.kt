package com.littlelemon.application.address.domain

import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun getLocation(): Resource<LocalLocation>

    suspend fun saveAddress(address: LocalAddress): Resource<Unit>

    suspend fun reverseGeocodeLocation(latLng: LocalLocation): Resource<GeocodedAddress>

    suspend fun geocodeAddress(fullAddress: String): Resource<GeocodedAddress>

    fun getAddress(): Flow<Resource<List<LocalAddress>>>
    suspend fun getAddressCount(): Int

    fun getCurrentAddress(): Flow<Resource<LocalAddress>>

    fun setCurrentAddress(address: LocalAddress): Flow<Resource<Unit>>

    suspend fun refreshCache(): Resource<Unit>

    suspend fun removeAddress(address: LocalAddress)
}