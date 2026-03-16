package com.littlelemon.application.address.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.google.maps.model.LatLng
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.local.dao.GeocodingDao
import com.littlelemon.application.address.data.mappers.toAddressEntity
import com.littlelemon.application.address.data.mappers.toLocalAddress
import com.littlelemon.application.address.data.mappers.toLocalLocation
import com.littlelemon.application.address.data.mappers.toRequestDTO
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.data.remote.geocoding.GeocodingRemoteDataSource
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.GeocodedAddress
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
    private val addressLocalDataSource: AddressLocalDataSource,
    private val addressRemoteDataSource: AddressRemoteDataSource,
    private val localGeocodingDatasource: GeocodingDao,
    private val remoteGeocodingDataSource: GeocodingRemoteDataSource
) : AddressRepository {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getLocation(): Resource<LocalLocation> {
        return try {
            val location = addressLocalDataSource.getLocation()
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
            val remoteData = addressRemoteDataSource.saveAddress(address.toRequestDTO())
            addressLocalDataSource.saveAddress(remoteData.toAddressEntity())
            Resource.Success()
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Resource.Failure(
                errorMessage = e.message,
                error = e.mapToDomainError()
            )
        }
    }

    override suspend fun reverseGeocodeLocation(latLng: LatLng): Resource<GeocodedAddress> {
        TODO("Not yet implemented")
    }

    override suspend fun geocodeAddress(fullAddress: String): Resource<GeocodedAddress> {
        val THIRTY_DAYS = 30 * 24 * 60 * 60 * 1000L
        TODO("Not yet implemented")
//        try {
////            localGeocodingDatasource.clearExpired(THIRTY_DAYS)
////            val cachedAddress = localGeocodingDatasource.getAddress(fullAddress)
////            if (cachedAddress != null)
////                return Resource.Success(cachedAddress.toGeocodedAddress())
////
////            val remoteAddress = remoteGeocodingDataSource.geocodeAddress(fullAddress)
////            localGeocodingDatasource.upsert(remoteAddress)
//        } catch (e: Exception) {
//            currentCoroutineContext().ensureActive()
//            Resource.Failure(
//                errorMessage = e.message,
//                error = e.mapToDomainError()
//            )
//        }
    }

    // TODO: Edge case fix, use deletes address from one device and logs into their another device
    // TODO: Try to refresh form supabase
    override fun getAddress(): Flow<Resource<List<LocalAddress>>> =
        flow {
            var offlineData: List<LocalAddress>? = null
            try {
                offlineData =
                    addressLocalDataSource.getAddress().first().map { it.toLocalAddress() }
                emit(Resource.Loading(offlineData))

                val remoteResponse =
                    addressRemoteDataSource.getAddress().map { it.toAddressEntity() }
                addressLocalDataSource.saveAddresses(remoteResponse)
                val newData = addressLocalDataSource.getAddress()
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

    override suspend fun getAddressCount(): Long {
        try {
            return addressLocalDataSource.getAddressCount()
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            return -1
        }
    }

}