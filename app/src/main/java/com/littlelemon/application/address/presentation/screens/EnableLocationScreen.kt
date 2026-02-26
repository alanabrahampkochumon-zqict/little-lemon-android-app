package com.littlelemon.application.address.presentation.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressActions
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonShape
import com.littlelemon.application.core.presentation.components.ButtonSize
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.components.DoodleBackground
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

const val FINE_LOCATION_PERMISSION_CODE = 100

@Composable
fun EnableLocationScreen(viewModel: AddressViewModel, modifier: Modifier = Modifier) {

    val activity = LocalActivity.current

    var showSettingsRedirect by remember { mutableStateOf(false) }
    val permissions =
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted =
            permissions.values.reduce { acc, isPermissionGranted -> acc && isPermissionGranted }

        if (isGranted) {
            // TODO: Permission Granted navigate
        } else {
            val shouldShowRationale =
                activity?.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    ?: false

            if (!shouldShowRationale) {
                viewModel.onAction(AddressActions.PermissionDenied)
            }
        }
    }
    EnableLocationScreenRoot(
        modifier = modifier,
        onEnableLocationClick = {
            permissionLauncher.launch(permissions)
        },
        onManualLocationClick = {
            viewModel.onAction(AddressActions.EnterLocationManually)
        }
    )
}

@Composable
fun EnableLocationScreenRoot(
    modifier: Modifier = Modifier,
    onEnableLocationClick: () -> Unit = {},
    onManualLocationClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars.only(
            WindowInsetsSides.Bottom
        ).add(WindowInsets.ime)
    ) { innerPadding ->
        DoodleBackground(alpha = 0.1f)
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MaterialTheme.dimens.sizeXL)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.enable_location_service),
                contentDescription = null,
                Modifier
                    .widthIn(max = 450.dp)
                    .offset(y = MaterialTheme.dimens.size2XL) // Offset applied to negate image's y height due to shadow
            )
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeXS)) {
                Text(
                    stringResource(R.string.heading_location),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typeStyle.displayLarge.copy(textAlign = TextAlign.Center),
                    color = MaterialTheme.colors.contentPrimary
                )
                Text(
                    stringResource(R.string.body_location),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typeStyle.bodyMedium.copy(textAlign = TextAlign.Center),
                    color = MaterialTheme.colors.contentSecondary
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.size4XL))

            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeLG),
                modifier = Modifier.widthIn(max = 480.dp)
            ) {
                Button(
                    label = stringResource(R.string.act_enable_location),
                    onClick = onEnableLocationClick,
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.PRIMARY,
                    size = ButtonSize.Medium
                )
                Button(
                    label = stringResource(R.string.act_enter_location_manually),
                    onClick = onManualLocationClick,
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.SECONDARY,
                    size = ButtonSize.Medium
                )
            }

        }

    }

}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    content: @Composable () -> Unit = {},
) {

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Box(Modifier.fillMaxSize().blur(MaterialTheme.dimens.sizeLG)){
            content()
            Box(Modifier.fillMaxSize().background(MaterialTheme.colors.darkOverlay24))
        }
        Box(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.sizeXL).clip(
            MaterialTheme.shapes.large).background(MaterialTheme.colors.primary)) {
            Column(
                modifier = Modifier,
            ) {
               Column(modifier = Modifier
                   .padding(MaterialTheme.dimens.size2XL)) {
                   Text(
                       "Make reservation",
                       style = MaterialTheme.typeStyle.displaySmall,
                       textAlign = TextAlign.Start,
                       color = MaterialTheme.colors.contentPrimary
                   )
                   Spacer(Modifier.height(MaterialTheme.dimens.sizeLG))
                   Text(
                       text = "You are about to reserve a table for 3, on 29th June, 2026 at 07:30 PM",
                       style = MaterialTheme.typeStyle.bodyMedium,
                       color = MaterialTheme.colors.contentSecondary
                   )
               }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        label = "Edit Reservation",
                        variant = ButtonVariant.GHOST,
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        buttonShape = ButtonShape.Square
                    )
                    Button(
                        label = "Make Reservation",
                        variant = ButtonVariant.PRIMARY,
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        buttonShape = ButtonShape.Square
                    )
                }
            }
        }
        }
    }




@Preview
@Composable
private fun AlertDialogPreview() {
    LittleLemonTheme {
        AlertDialogExample(
            {},
            {},
            "Test",
            "This is some test content",
        ) {
            EnableLocationScreenRoot()
        }
    }
}

private fun hasLocationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

@Preview
@Composable
private fun EnableLocationScreenRootPreview() {
    LittleLemonTheme {
        EnableLocationScreenRoot()
    }
}