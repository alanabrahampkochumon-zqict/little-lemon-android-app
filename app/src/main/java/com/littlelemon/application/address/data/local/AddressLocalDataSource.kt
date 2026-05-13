package com.littlelemon.application.address.data.local

import android.location.Location
import com.littlelemon.application.database.address.models.AddressEntity
import kotlinx.coroutines.flow.Flow

interface AddressLocalDataSource {

    suspend fun getLocation(): Location

    fun getAddress(): Flow<List<AddressEntity>>

    suspend fun saveAddress(address: AddressEntity)

    suspend fun saveAddresses(addresses: List<AddressEntity>)

    suspend fun getAddressCount(): Int

    suspend fun removeAddress(addressId: String): AddressEntity

    suspend fun clearAndInsertAddress(addresses: List<AddressEntity>)
}