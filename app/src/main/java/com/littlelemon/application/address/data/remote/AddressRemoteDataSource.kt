package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.remote.models.AddressDTO

interface AddressRemoteDataSource {

    suspend fun getAddress(): List<AddressDTO>

    suspend fun saveAddress(address: AddressDTO)
    
}