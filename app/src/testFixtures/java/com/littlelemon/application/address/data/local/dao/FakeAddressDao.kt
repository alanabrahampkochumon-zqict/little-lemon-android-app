package com.littlelemon.application.address.data.local.dao

import com.littlelemon.application.address.utils.AddressGenerator
import com.littlelemon.application.database.address.dao.AddressDao
import com.littlelemon.application.database.address.models.AddressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAddressDao(entriesCount: Int? = null, private val throwError: Boolean = false) :
    AddressDao {

    private val addressList = mutableListOf<AddressEntity>()

    init {
        if (entriesCount != null) {
            repeat(entriesCount) {
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

    override suspend fun getAddressCount(): Int {
        if (throwError) throw IllegalArgumentException()
        return addressList.size
    }

    override fun getAllAddress(): Flow<List<AddressEntity>> = flow {
        if (throwError) throw IllegalArgumentException()
        emit(addressList)
    }

    override suspend fun deleteAddress(address: AddressEntity): Int {
        if (throwError) throw IllegalArgumentException()
        return if (addressList.remove(address)) 1 else 0
    }

    override suspend fun clear(): Int {
        if (throwError) throw IllegalArgumentException()
        val count = addressList.size
        addressList.clear()
        return count
    }

    fun getAddressList(): List<AddressEntity> = addressList.toList()

    override suspend fun clearAndInsertAllAddress(addresses: List<AddressEntity>) {
        if (throwError) throw IllegalArgumentException()
        addressList.clear()
        addressList.addAll(addresses)
    }
}