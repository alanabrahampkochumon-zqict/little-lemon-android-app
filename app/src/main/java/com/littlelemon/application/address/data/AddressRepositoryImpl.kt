package com.littlelemon.application.address.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.mappers.toLocalLocation
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.utils.Resource

class AddressRepositoryImpl(
    private val localDataSource: AddressLocalDataSource,
    private val remoteDataSource: AddressRemoteDataSource
) : AddressRepository {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getLocation(): Resource<LocalLocation> {
        return try {
            val location = localDataSource.getLocation()
            Resource.Success(location.toLocalLocation())
        } catch (e: Exception) {
            //TODO Save the location to db
            Resource.Failure(errorMessage = "Failed to fetch location")
        }
    }

    override suspend fun saveAddress(address: LocalAddress): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAddress(): Resource<List<LocalAddress>> {
        TODO("Not yet implemented")
    }
}