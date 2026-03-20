package com.littlelemon.application.core.presentation.utils

import android.Manifest
import android.app.Activity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun requestLocationPermission(
    onLocationGranted: () -> Unit,
    onLocationPermissionDenied: () -> Unit,
    activity: Activity,
    onLocationDeclined: () -> Unit = {},
): ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>> {
    val locationRequestLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                onLocationGranted()
            } else
                onLocationDeclined()
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted =
            permissions.values.all { it }

        if (isGranted) {
            checkLocationSetting(
                activity,
                onLocationEnabled =
                    onLocationGranted,
                onStartResolution = { exception ->
                    locationRequestLauncher.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )
                })
        } else {
            val shouldShowRationale =
                activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            if (!shouldShowRationale)
                onLocationPermissionDenied()
        }
    }
    return permissionLauncher
}