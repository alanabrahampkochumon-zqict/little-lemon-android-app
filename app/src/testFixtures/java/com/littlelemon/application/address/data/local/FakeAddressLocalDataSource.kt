package com.littlelemon.application.address.data.local

import android.location.Location
import com.littlelemon.application.address.data.local.models.AddressEntity
import com.littlelemon.application.utils.AddressGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAddressLocalDataSource(
    initialDataCount: Int? = null,
    private val throwError: Boolean = false,
    private val location: Location? = null
) : AddressLocalDataSource {

    private val _address = mutableListOf<AddressEntity>()

    init {
        initialDataCount?.let { count ->
            repeat(count) {
                _address.add(AddressGenerator.generateAddressEntity())
            }
        }
    }

    override suspend fun getLocation(): Location {
        if (throwError) throw IllegalArgumentException()
        return location ?: throw IllegalArgumentException()
    }

    override fun getAddress(): Flow<List<AddressEntity>> = flow {
        if (throwError) throw IllegalArgumentException()
        emit(_address)
    }

    override suspend fun saveAddress(address: AddressEntity) {
        if (throwError) throw IllegalArgumentException()
        _address.removeIf { inAddress -> inAddress.id == address.id }
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