package com.littlelemon.application.address.data.local

import android.location.Location
import com.littlelemon.application.address.data.local.models.AddressEntity
import kotlinx.coroutines.flow.Flow

interface AddressLocalDataSource {

    suspend fun getLocation(): Location

    fun getAddress(): Flow<List<AddressEntity>>

    suspend fun saveAddress(address: AddressEntity)

    suspend fun getAddressCount(): Long
}