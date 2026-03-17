package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.littlelemon.application.address.presentation.screens.FloatingActionBar


@Composable
fun MapHeader(
    modifier: Modifier, onClose: () -> Unit, floatingBarTopPadding: Dp, floatingBarBottomPadding: Dp
) {
    val singapore = LatLng(1.35, 103.87)
    val singaporeMarkerState = rememberUpdatedMarkerState(position = singapore)
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(singapore, 10f).also { position = it }
    }
    val uiSetting =
        remember { MapUiSettings(zoomControlsEnabled = true, zoomGesturesEnabled = true) }
    Box(
        modifier = modifier, contentAlignment = Alignment.TopCenter
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSetting,
            properties = MapProperties(mapType = MapType.NORMAL),
            mapColorScheme = ComposeMapColorScheme.LIGHT
        ) {
            Marker(
                state = singaporeMarkerState, title = "Singapore", snippet = "Marker in Singapore"
            )
        }

        FloatingActionBar(
            modifier = Modifier.padding(
                top = floatingBarTopPadding, bottom = floatingBarBottomPadding
            ), onAction = onClose
        )

    }
}