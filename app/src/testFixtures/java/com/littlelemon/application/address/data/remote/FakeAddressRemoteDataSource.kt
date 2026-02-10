package com.littlelemon.application.address.data.remote

import com.littlelemon.application.address.data.mappers.toResponse
import com.littlelemon.application.address.data.remote.models.AddressDTO
import com.littlelemon.application.address.data.remote.models.AddressRequestDTO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun saveAddress(address: AddressRequestDTO): AddressDTO {
        if (throwError) throw IllegalArgumentException()
        _address.removeIf { (id) -> address.id == id }
        val newAddress =
            if (address.id == null) address.copy(id = address.id ?: Uuid.random().toString())
                .toResponse() else address.toResponse()
        _address.add(
            newAddress
        )
        return newAddress
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