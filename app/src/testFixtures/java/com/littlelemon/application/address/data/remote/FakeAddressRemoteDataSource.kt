package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.mappers.toResponse
import com.littlelemon.application.address.data.remote.models.AddressDTO
import com.littlelemon.application.address.data.remote.models.AddressRequestDTO

class FakeAddressRemoteDataSource(
    initialData: List<AddressDTO> = emptyList(),
    private val throwError: Boolean = false,
) : AddressRemoteDataSource {

    private val _address = mutableListOf<AddressDTO>()

    init {
        _address.addAll(initialData)
    }

    override suspend fun getAddress(): List<AddressDTO> {
        if (throwError) throw IllegalArgumentException()
        return _address
    }

    override suspend fun saveAddress(address: AddressRequestDTO): AddressDTO {
        if (throwError) throw IllegalArgumentException()
        _address.removeIf { (id) -> address.id == id }
        _address.add(address.toResponse())
        return address.toResponse()
    }

    override suspend fun updateAddress(address: AddressRequestDTO): AddressDTO {
        if (throwError) throw IllegalArgumentException()
        _address.remove(address.toResponse())
        _address.add(address.toResponse())
        return address.toResponse()
    }

    override suspend fun deleteAddress(address: AddressRequestDTO) {
        if (throwError) throw IllegalArgumentException()
        _address.remove(address.toResponse())
    }
}