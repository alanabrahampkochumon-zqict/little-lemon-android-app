package com.littlelemon.application.address.data.local

import android.location.Location
import com.littlelemon.application.address.data.local.models.AddressEntity
import com.littlelemon.application.core.domain.exceptions.LocationUnavailableException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAddressLocalDataSource(
    initialData: List<AddressEntity> = emptyList(),
    private val throwError: Boolean = false,
    private val location: Location? = null
) : AddressLocalDataSource {

    private val _address = mutableListOf<AddressEntity>()

    init {
        _address.addAll(initialData)
    }

    override suspend fun getLocation(): Location {
        if (throwError) throw LocationUnavailableException()
        return location ?: throw LocationUnavailableException()
    }

    override fun getAddress(): Flow<List<AddressEntity>> = flow {
        if (throwError) throw IllegalArgumentException()
        emit(_address)
    }

    override suspend fun saveAddress(address: AddressEntity) {
        if (throwError) throw IllegalArgumentException()
        _address.remove(address)
        _address.add(address)
    }

    override suspend fun saveAddresses(addresses: List<AddressEntity>) {
        if (throwError) throw IllegalArgumentException()
        addresses.forEach { inAddress -> _address.removeIf { (id) -> id == inAddress.id } }
        _address.addAll(addresses)
    }

    override suspend fun getAddressCount(): Long {
        if (throwError) throw IllegalArgumentException()
        return _address.size.toLong()
    }
}