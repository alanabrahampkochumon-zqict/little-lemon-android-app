package com.littlelemon.application.address.data.local

import android.Manifest
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class AddressLocalDataSource(
    private val locationProvider: FusedLocationProviderClient
) {
    companion object {
        private const val LOCATION_STALE_TIME = 10 * 60 * 1000L
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getLocation(): Location {
        val staleTime = System.currentTimeMillis() - LOCATION_STALE_TIME

        val lastLocation = try {
            locationProvider.lastLocation.await()
        } catch (_: Exception) {
            null
        }

        if (lastLocation != null && lastLocation.time > staleTime) {
            return lastLocation
        }

        val newLocation = locationProvider.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).await()

        // 4. Return or Throw (Handle the case where GPS returns null)
        return newLocation ?: throw Exception("Unable to fetch current location")
    }
}