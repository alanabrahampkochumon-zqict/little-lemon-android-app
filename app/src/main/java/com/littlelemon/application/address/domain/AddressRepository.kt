package com.littlelemon.application.address.domain

import com.google.maps.model.LatLng
import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun getLocation(): Resource<LocalLocation>

    suspend fun saveAddress(address: LocalAddress): Resource<Unit>

    suspend fun geocodeLocation(latLng: LatLng): Resource<GeocodedAddress>

    suspend fun reverseGeocodeAddress(fullAddress: String): Resource<GeocodedAddress>

    fun getAddress(): Flow<Resource<List<LocalAddress>>>
    suspend fun getAddressCount(): Long
}