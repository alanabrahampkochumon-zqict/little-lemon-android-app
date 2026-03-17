package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import com.littlelemon.application.BuildConfig
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.screens.FloatingActionBar
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle


@Composable
fun MapHeader(
    onClose: () -> Unit,
    floatingBarTopPadding: Dp,
    floatingBarBottomPadding: Dp,
    modifier: Modifier = Modifier,
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


        if (BuildConfig.MAPS_API_KEY.isBlank())
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colors.primaryDark
                    )
                    .padding(MaterialTheme.dimens.sizeXL)
            ) {
                // Content shown only when a Google map api key is not provided
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeSM),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.no_gmap_api_key_header),
                        style = MaterialTheme.typeStyle.displayLarge.copy(textAlign = TextAlign.Center),
                        color = MaterialTheme.colors.contentOnColor
                    )

                    Text(
                        stringResource(R.string.no_gmap_api_key_body),
                        style = MaterialTheme.typeStyle.bodyMedium.copy(textAlign = TextAlign.Center),
                        color = MaterialTheme.colors.contentOnColor
                    )
                }
            }
        else
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSetting,
                properties = MapProperties(mapType = MapType.NORMAL),
                mapColorScheme = ComposeMapColorScheme.LIGHT
            ) {
                Marker(
                    state = singaporeMarkerState,
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }

        FloatingActionBar(
            modifier = Modifier.padding(
                top = floatingBarTopPadding, bottom = floatingBarBottomPadding
            ), onAction = onClose
        )

    }
}

@Preview
@Composable
private fun MapHeaderPreview() {
    LittleLemonTheme {
        MapHeader(onClose = {}, floatingBarTopPadding = 0.dp, floatingBarBottomPadding = 0.dp)
    }
}