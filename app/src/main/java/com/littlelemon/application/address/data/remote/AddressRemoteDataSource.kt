package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.remote.models.AddressDTO
import com.littlelemon.application.address.data.remote.models.AddressRequestDTO

interface AddressRemoteDataSource {
    suspend fun getAddress(): List<AddressDTO>
    suspend fun saveAddress(address: AddressRequestDTO): AddressDTO
    suspend fun deleteAddress(address: AddressRequestDTO)

    suspend fun geocodeAddress(address: AddressRequestDTO)

//    suspend fun reverseGeocodeAddress(latLng: LatLng)
}