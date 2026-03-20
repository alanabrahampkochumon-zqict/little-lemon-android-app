package com.littlelemon.application.core.presentation.utils

import android.app.Activity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

fun checkLocationSetting(
    activity: Activity,
    onLocationEnabled: () -> Unit,
    requestCode: Int
) {
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .build()
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val client = LocationServices.getSettingsClient(activity.applicationContext!!)
    client.checkLocationSettings(builder.build())
        .addOnSuccessListener {
            onLocationEnabled()
        }
        .addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(activity, requestCode)
                } catch (e: Exception) {
                }
            }
        }
}
