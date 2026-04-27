package com.littlelemon.application.address.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.mappers.toAddressEntity
import com.littlelemon.application.address.data.mappers.toGeocodedAddress
import com.littlelemon.application.address.data.mappers.toGeocodingEntity
import com.littlelemon.application.address.data.mappers.toLocalAddress
import com.littlelemon.application.address.data.mappers.toLocalLocation
import com.littlelemon.application.address.data.mappers.toRequestDTO
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.data.remote.geocoding.GeocodingRemoteDataSource
import com.littlelemon.application.address.data.remote.models.GeocodingDTO
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.GeocodedAddress
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.exceptions.LocationUnavailableException
import com.littlelemon.application.core.domain.mappers.mapToDomainError
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.database.address.dao.GeocodingDao
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.time.Clock

class DefaultAddressRepository(
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
    // TODO: Implement better caching, hit once, fetch on save
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

    override suspend fun reverseGeocodeLocation(latLng: LocalLocation): Resource<GeocodedAddress> {
        val THIRTY_DAYS = 30 * 24 * 60 * 60 * 1000L
        val expiry = Clock.System.now().toEpochMilliseconds() - THIRTY_DAYS
        return try {
            // Fetching from cache
            localGeocodingDatasource.clearExpired(expiry)
            val cachedAddress =
                localGeocodingDatasource.getAddress(latLng.latitude, latLng.longitude)
            if (cachedAddress != null)
                return Resource.Success(cachedAddress.toGeocodedAddress())
            // If cache is expired or has no data, fetch from remote
            val remoteAddress = remoteGeocodingDataSource.reverseGeocodeAddress(
                GeocodingDTO.LatLng(
                    latLng.latitude,
                    latLng.longitude
                )
            )
            localGeocodingDatasource.upsert(remoteAddress.toGeocodingEntity())

            val newCache = localGeocodingDatasource.getAddress(latLng.latitude, latLng.longitude)
            Resource.Success(newCache?.toGeocodedAddress())
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Resource.Failure(
                errorMessage = e.message,
                error = e.mapToDomainError()
            )
        }
    }

    override suspend fun geocodeAddress(fullAddress: String): Resource<GeocodedAddress> {
        val THIRTY_DAYS = 30 * 24 * 60 * 60 * 1000L
        val expiry = Clock.System.now().toEpochMilliseconds() - THIRTY_DAYS
        return try {
            // Fetching from cache
            localGeocodingDatasource.clearExpired(expiry)
            val cachedAddress = localGeocodingDatasource.getAddress(fullAddress)
            if (cachedAddress != null)
                return Resource.Success(cachedAddress.toGeocodedAddress())
            // If cache is expired or has no data, fetch from remote
            val remoteAddress = remoteGeocodingDataSource.geocodeAddress(fullAddress)
            localGeocodingDatasource.upsert(remoteAddress.toGeocodingEntity())

            val newCache = localGeocodingDatasource.getAddress(remoteAddress.fullAddress)
            Resource.Success(newCache?.toGeocodedAddress())
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

    override suspend fun getAddressCount(): Int {
        try {
            // TODO: Remove and work with only cached data
            val remoteResponse =
                addressRemoteDataSource.getAddress().map { it.toAddressEntity() }
            addressLocalDataSource.saveAddresses(remoteResponse)
            return addressLocalDataSource.getAddressCount()
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            return -1
        }
    }

    override fun getCurrentAddress(): Flow<Resource<LocalAddress>> {
        return addressLocalDataSource.getAddress().map { addressEntities ->
            // Get the default address if any
            var entity = addressEntities.firstOrNull { entity -> entity.isDefault }
            // If it fails get the first address
            if (entity == null)
                entity = addressEntities.firstOrNull()
            if (entity != null)
                return@map Resource.Success(entity.toLocalAddress())
            // No address entity exists in the cache.
            Resource.Success(null)
        }
    }

    override fun setCurrentAddress(address: LocalAddress): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            addressRemoteDataSource.saveAddress(address.copy(isDefault = true).toRequestDTO())
            refreshCache()
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            emit(
                Resource.Failure(
                    data = Unit,
                    errorMessage = e.message,
                    error = e.mapToDomainError()
                )
            )
        }

    }

    override suspend fun refreshCache(): Resource<Unit> {
        try {
            val addressEntities =
                addressRemoteDataSource.getAddress().map { it.toAddressEntity() }
            addressLocalDataSource.clearAndInsertAddress(addressEntities)
            return Resource.Success()
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            return Resource.Failure(
                data = Unit,
                errorMessage = e.message,
                error = e.mapToDomainError()
            )
        }
    }
}
