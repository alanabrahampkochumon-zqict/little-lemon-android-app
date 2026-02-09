package com.littlelemon.application.address.data.local.dao

import com.littlelemon.application.address.data.local.models.AddressEntity
import com.littlelemon.application.utils.AddressGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAddressDao(populate: Boolean = false, private val throwError: Boolean = false) :
    AddressDao {

    private val addressList = mutableListOf<AddressEntity>()

    companion object {
        const val NUM_ADDRESSES = 20
    }

    init {
        if (populate) {
            repeat(NUM_ADDRESSES) {
                addressList.add(AddressGenerator.generateAddressEntity())
            }
        }
    }

    override suspend fun insertAddress(addresses: List<AddressEntity>) {
        if (throwError) throw IllegalArgumentException()
        addressList.addAll(addresses)
    }

    override suspend fun insertAddress(address: AddressEntity) {
        if (throwError) throw IllegalArgumentException()
        addressList.add(address)
    }

    override suspend fun getAddressCount(): Long {
        if (throwError) throw IllegalArgumentException()
        return addressList.size.toLong()
    }

    override fun getAllAddress(): Flow<List<AddressEntity>> = flow {
        if (throwError) throw IllegalArgumentException()
        emit(addressList)
    }
}