package com.littlelemon.application.address.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.mappers.toAddressEntity
import com.littlelemon.application.address.data.mappers.toLocalAddress
import com.littlelemon.application.address.data.mappers.toLocalLocation
import com.littlelemon.application.address.data.mappers.toRequestDTO
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.exceptions.LocationUnavailableException
import com.littlelemon.application.core.domain.mappers.mapToDomainError
import com.littlelemon.application.core.domain.utils.Resource
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class AddressRepositoryImpl(
    private val localDataSource: AddressLocalDataSource,
    private val remoteDataSource: AddressRemoteDataSource
) : AddressRepository {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getLocation(): Resource<LocalLocation> {
        return try {
            val location = localDataSource.getLocation()
            Resource.Success(location.toLocalLocation())
            // TODO: Do geocoding
            // TODO: Store location as address
        } catch (e: LocationUnavailableException) {
            Resource.Failure(errorMessage = e.message)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Resource.Failure(
                errorMessage = e.message,
                error = e.mapToDomainError()
            )
        }

    }

    //TODO: Add Service to retry saving to network in case of network error.
    override suspend fun saveAddress(address: LocalAddress): Resource<Unit> {
        return try {
            if (address.id == null) { // Creation
                val remoteData = remoteDataSource.saveAddress(address.toRequestDTO())
                localDataSource.saveAddress(remoteData.toAddressEntity())
            } else { // Update
                val remoteData = remoteDataSource.updateAddress(address.toRequestDTO())
                localDataSource.saveAddress(remoteData.toAddressEntity())
            }
            Resource.Success()
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Resource.Failure(
                errorMessage = e.message,
                error = e.mapToDomainError()
            )
        }
    }

    override fun getAddress(): Flow<Resource<List<LocalAddress>>> =
        flow {
            var offlineData: List<LocalAddress>? = null
            try {
                offlineData =
                    localDataSource.getAddress().first().map { it.toLocalAddress() }
                emit(Resource.Loading(offlineData))

                val remoteResponse = remoteDataSource.getAddress().map { it.toAddressEntity() }
                localDataSource.saveAddresses(remoteResponse)
                val newData = localDataSource.getAddress()
                    .map { addresses -> Resource.Success(data = addresses.map { it.toLocalAddress() }) }
                emitAll(
                    flow = newData
                )
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()
                emit(
                    Resource.Failure(
                        data = offlineData,
                        errorMessage = e.message,
                        error = e.mapToDomainError()
                    )
                )
            }
        }.onStart {
            emit(Resource.Loading<List<LocalAddress>>(null))
        }

}