package com.littlelemon.application.address.data

import android.location.Location
import com.littlelemon.application.address.data.local.AddressLocalDataSource
import com.littlelemon.application.address.data.remote.AddressRemoteDataSource
import com.littlelemon.application.core.domain.exceptions.LocationUnavailableException
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class AddressRepositoryImplTest {

    private val localDataSource = mockk<AddressLocalDataSource>()
    private val remoteDataSource = mockk<AddressRemoteDataSource>()

    private val repository = AddressRepositoryImpl(
        localDataSource,
        remoteDataSource
    )

    private companion object {
        const val LATITUDE = 1.2345
        const val LONGITUDE = 3.2354
    }

    @Test
    fun onGetLocation_dataSourceSuccess_returnsResourceSuccess() = runTest {
        // Arrange
        val location = mockk<Location>()
        every { location.latitude } returns LATITUDE
        every { location.longitude } returns LONGITUDE
        every { location.accuracy } returns 5.0f

        coEvery { localDataSource.getLocation() } returns location

        // Act
        val result = repository.getLocation()

        // Assert
        assertTrue(result is Resource.Success)
        val data = (result as Resource.Success).data
        assertNotNull(data)
        assertEquals(LATITUDE, data.latitude)
        assertEquals(LONGITUDE, data.longitude)
    }

    @Test
    fun onGetLocation_dataSourceError_returnsResourceError() = runTest {
        // Arrange
        coEvery { localDataSource.getLocation() } throws LocationUnavailableException()

        // Act
        val result = repository.getLocation()

        // Assert
        assertTrue(result is Resource.Failure)
    }

    //TODO: Test Implementation
    @Test
    fun onGetAddress_remoteFailureLocalCacheNonEmpty_returnsSuccessWithLocalCache() = runTest { }

    @Test
    fun onGetAddress_remoteSuccess_returnsSuccessWithData() = runTest {}

    @Test
    fun onGetAddress_remoteSuccessEmptyAddress_returnsSuccessWithEmptyData() = runTest { }

    @Test
    fun onSaveAddress_remoteSuccess_returnsResourceSuccess() = runTest { }

    @Test
    fun onSaveAddress_remoteFailure_returnsResourceFailure() = runTest { }
}