package com.littlelemon.application.address.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.mappers.toLocalLocation
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
import com.littlelemon.application.core.domain.exceptions.LocationUnavailableException
import com.littlelemon.application.core.domain.utils.Resource
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        flow<Resource<List<LocalAddress>>> {
            try {
                val offlineAddress = localDataSource.getAddress()
                val offlineAddressCount = localDataSource.getAddressCount()
            } catch (e: PostgrestRestException) {

            } catch (e: IllegalStateException) {

            } catch (e: Exception) {
            }
            TODO()
        }.onStart {
            emit(Resource.Loading<List<LocalAddress>>(null))
        }

}