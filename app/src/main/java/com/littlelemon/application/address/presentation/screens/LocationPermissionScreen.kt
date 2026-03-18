package com.littlelemon.application.address.presentation.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressActions
import com.littlelemon.application.address.presentation.AddressEvents
import com.littlelemon.application.address.presentation.AddressState
import com.littlelemon.application.address.presentation.AddressViewModel
import com.littlelemon.application.core.presentation.components.AlertDialog
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonSize
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.components.DoodleBackground
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationPermissionScreen(viewModel: AddressViewModel, modifier: Modifier = Modifier) {

    val activity = LocalActivity.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showLocationEntryDialog by remember { mutableStateOf(false) }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun getLocation() {
        viewModel.onAction(AddressActions.GetLocation)
    }

    var checkLocationSettingsTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(checkLocationSettingsTrigger) {

    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted =
            permissions.values.reduce { acc, isPermissionGranted -> acc && isPermissionGranted }

        if (isGranted) {
            checkLocationSetting(activity!!)
            getLocation()
            Log.d("Location", "${state.latitude}, ${state.longitude}")
            // TODO: Permission Granted: navigate
            // TODO: Auto take location
        } else {
            val shouldShowRationale =
                activity?.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    ?: false

            if (!shouldShowRationale) {
                viewModel.onAction(AddressActions.ShowLocationDialog)
            }
        }
    }


    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewModel.addressEvents, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

            viewModel.addressEvents.collect { event ->
                when (event) {
                    AddressEvents.AddressSaved -> Toast.makeText(
                        context,
                        "Address Saved!",
                        Toast.LENGTH_SHORT
                    ).show()

                    is AddressEvents.ShowError -> Toast.makeText(
                        context,
                        event.errorMessage.asString(context),
                        Toast.LENGTH_SHORT
                    ).show()

                    is AddressEvents.ShowInfo -> Toast.makeText(
                        context,
                        event.message.asString(context),
                        Toast.LENGTH_SHORT
                    ).show()

                    AddressEvents.ShowLocationEntryPopup -> showLocationEntryDialog = true
                }
            }
        }

    }

    Box(modifier) {

        LocationPermissionScreenRoot(state = state, modifier = modifier, onEnableLocationClick = {
            permissionLauncher.launch(permissions)
        }, onManualLocationClick = {
            viewModel.onAction(AddressActions.EnterLocationManually)
            viewModel.onAction(AddressActions.DismissLocationDialog)
        }, onDismissAlert = {
            viewModel.onAction(AddressActions.DismissLocationDialog)
        }, onAllowLocationAccessClick = {
            openAppSettings(activity?.applicationContext!!)
            viewModel.onAction(AddressActions.DismissLocationDialog)
        })
        AnimatedVisibility(
            showLocationEntryDialog,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
        ) {
            LocationEntryContent(
                koinViewModel<AddressViewModel>(),
                modifier = Modifier.fillMaxSize(),
                onClose = { showLocationEntryDialog = false })
        }
    }

}

@Composable
fun LocationPermissionScreenRoot(
    state: AddressState,
    modifier: Modifier = Modifier,
    onDismissAlert: () -> Unit = {},
    onEnableLocationClick: () -> Unit = {},
    onManualLocationClick: () -> Unit = {},
    onAllowLocationAccessClick: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(), contentWindowInsets = WindowInsets.systemBars.only(
            WindowInsetsSides.Bottom
        ).add(WindowInsets.ime)
    ) { innerPadding ->
        AlertDialog(
            dialogTitle = stringResource(R.string.dialog_title_delivery_address_needed),
            dialogText = stringResource(R.string.dialog_body_delivery_address_needed),
            showDialog = state.showLocationDialog,
            positiveActionText = stringResource(R.string.dialog_act_allow_access),
            onPositiveAction = onAllowLocationAccessClick,
            negativeActionText = stringResource(R.string.dialog_act_enter_manually),
            onDismissDialog = onDismissAlert,
            onNegativeAction = onManualLocationClick
        ) {
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
                        stringResource(R.string.heading_location_permission),
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
}


private fun checkLocationSetting(activity: Activity) {
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .build()
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val client = LocationServices.getSettingsClient(activity.applicationContext!!)
    Log.d("Location", "Running checks")
    client.checkLocationSettings(builder.build())
        .addOnSuccessListener {
            Log.d("Location", "Setting activated")
        }
        .addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    // GPS IS OFF: This shows the "Turn on GPS" system dialog
                    exception.startResolutionForResult(activity, 5001)
                } catch (e: Exception) {
                    Log.e("Location", "Error starting resolution", e)
                }
            }
        }
}


private fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    ).apply {
        addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )
    }
    context.startActivity(intent)
}

@Preview
@Composable
private fun LocationPermissionScreenRootPreview() {
    LittleLemonTheme {
        LocationPermissionScreenRoot(AddressState())
    }
}

@Preview
@Composable
private fun LocationPermissionScreenDialogPreview() {
    LittleLemonTheme {
        LocationPermissionScreenRoot(AddressState(showLocationDialog = true))
    }
}