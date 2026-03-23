package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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
import com.littlelemon.application.address.presentation.AddressTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xLarge
import com.littlelemon.application.core.presentation.utils.toComposeShadow


// TODO: Fix navigation button not showing on landscape
// TODO: Add address pin
@Composable
fun MapHeader(
    floatingBarTopPadding: Dp,
    floatingBarBottomPadding: Dp,
    onClose: () -> Unit,
    onFetchCurrentLocation: () -> Unit,
    modifier: Modifier = Modifier,
    myLatitude: Double? = null,
    myLongitude: Double? = null,
    isMyLocationEnabled: Boolean = false,
) {

    val buttonShape = MaterialTheme.shapes.xLarge
    val density = LocalDensity.current.density
    // TODO: For non location render a arbitrary pin
    val myLocation = LatLng(myLatitude ?: 10.1234, myLongitude ?: 103.123)
    val singaporeMarkerState = rememberUpdatedMarkerState(position = myLocation)
    val cameraPositionState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(myLocation, 10f).also { position = it }
    }
    val uiSetting =
        remember { MapUiSettings(zoomControlsEnabled = false, zoomGesturesEnabled = true) }
    Box(
        modifier = modifier, contentAlignment = Alignment.TopCenter
    ) {

        if (BuildConfig.MAPS_API_KEY.isBlank())
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colors.secondary
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.sizeXL),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconButton(
                onClick = onFetchCurrentLocation,
                modifier = Modifier
                    .dropShadow(
                        buttonShape,
                        MaterialTheme.shadows.dropSM.firstShadow.toComposeShadow(density)
                    )
                    .background(MaterialTheme.colors.primary, buttonShape)
                    .testTag(AddressTestTags.MAP_HEADER_FETCH_LOCATION_BUTTON)
            ) {
                Image(painterResource(R.drawable.ic_gpsfix), null)
            }
        }


        FloatingActionBar(
            stringResource(R.string.heading_add_your_address),
            onAction = onClose,
            modifier = Modifier.padding(
                top = floatingBarTopPadding, bottom = floatingBarBottomPadding
            ),
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun MapHeaderPreview() {
    LittleLemonTheme {
        MapHeader(
            onClose = {},
            onFetchCurrentLocation = {},
            floatingBarTopPadding = 16.dp,
            floatingBarBottomPadding = 0.dp
        )
    }
}