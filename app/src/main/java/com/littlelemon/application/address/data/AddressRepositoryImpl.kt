package com.littlelemon.application.address.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.mappers.toAddressEntity
import com.littlelemon.application.address.data.mappers.toLocalAddress
import com.littlelemon.application.address.data.mappers.toLocalLocation
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.exceptions.LocationUnavailableException
import com.littlelemon.application.core.domain.utils.Error
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.domain.utils.toNetworkError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.ktor.client.plugins.HttpRequestTimeoutException
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
            // TODO: Handle Exception thrown for saving as well
            // TODO: Handle DB Exceptions
            Resource.Failure(errorMessage = e.message)
        } finally {
            currentCoroutineContext().ensureActive()
        }

    }

    override suspend fun saveAddress(address: LocalAddress): Resource<Unit> {
        TODO("Not yet implemented")
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

            } catch (e: PostgrestRestException) {
                emit(
                    Resource.Failure(
                        data = offlineData,
                        errorMessage = e.message,
                        error = e.code?.toInt()?.toNetworkError()
                    )
                )
            } catch (e: HttpRequestTimeoutException) {
                emit(
                    Resource.Failure(
                        data = offlineData,
                        errorMessage = e.message,
                        error = Error.NetworkError.Timeout()
                    )
                )
            } catch (e: HttpRequestException) {
                emit(
                    Resource.Failure(
                        data = offlineData,
                        errorMessage = e.message,
                        error = Error.NetworkError.Unknown()
                    )
                )
            } catch (e: IllegalStateException) {
                emit(
                    Resource.Failure(
                        data = offlineData,
                        errorMessage = e.message,
                        error = Error.Database
                    )
                )
            } catch (e: Exception) {
                emit(
                    Resource.Failure(
                        data = offlineData,
                        errorMessage = e.message,
                        error = Error.Unknown
                    )
                )
                currentCoroutineContext().ensureActive()
            }
        }.onStart {
            emit(Resource.Loading<List<LocalAddress>>(null))
        }

}