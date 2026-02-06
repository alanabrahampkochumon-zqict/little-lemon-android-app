package com.littlelemon.application.address.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.mappers.toLocalLocation
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.utils.Resource

class AddressRepositoryImpl(
    private val localDataSource: AddressLocalDataSource
) : AddressRepository {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getLocation(): Resource<LocalLocation> {
        return try {
            val location = localDataSource.getLocation()
            Resource.Success(location.toLocalLocation())
        } catch (e: Exception) {
            //TODO
            Resource.Failure(errorMessage = "Failed to fetch location")
        }
    }
}