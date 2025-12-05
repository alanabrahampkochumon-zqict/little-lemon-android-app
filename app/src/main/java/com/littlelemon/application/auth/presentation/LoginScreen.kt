package com.littlelemon.application.auth.presentation

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.components.Button
import com.littlelemon.application.core.presentation.designsystem.components.TextInputField
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@Composable
fun LoginScreen(onSendOtp: () -> Unit, modifier: Modifier = Modifier) {

//    val viewModel = koinViewModel<AuthViewModel>()

    val illustrationAspectRatio = (375.0 / 402.0).toFloat()

    var emailAddress by remember {
        mutableStateOf("")
    }
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    // Forcing status bar content to be dark
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Force Icons to be BLACK (True) because the image is Light
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    // Handle Orientation
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    val windowInsets = if(orientation == Configuration.ORIENTATION_PORTRAIT) {
        WindowInsets.systemBars.only(
            WindowInsetsSides.Bottom
        )
    } else {
        WindowInsets.systemBars.only(
            WindowInsetsSides.Bottom
        ).add(WindowInsets.displayCutout)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colors.primary,
        contentWindowInsets = windowInsets
    ) { innerPadding ->

        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                        .height(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Hero Image
                    Image(
                        painter = painterResource(R.drawable.illustration_people_dining),
                        contentDescription = null,
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(illustrationAspectRatio),
                        contentScale = ContentScale.FillBounds
                    )

                    Content(
                        emailAddress,
                        onValueChange = { newEmail ->
                            emailAddress = newEmail
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = MaterialTheme.dimens.spacingLG,
                                end = MaterialTheme.dimens.spacingLG,
                                top = MaterialTheme.dimens.spacingXL,
                                bottom = MaterialTheme.dimens.spacingXL,
                            ),
                        errorMessage = errorMessage,
                        onSendOtp = onSendOtp
                    )
                }
            }


            else -> {
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacingLG)
                ) {
                    // Hero Image
                    Image(
                        painter = painterResource(R.drawable.illustration_people_dining),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                end = MaterialTheme.dimens.spacing2XL
                            )
                            .imePadding()
                            .widthIn(max = 488.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {


                        Content(
                            emailAddress,
                            onValueChange = { newEmail ->
                                emailAddress = newEmail
                            },
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            errorMessage = errorMessage,
                            onSendOtp = onSendOtp
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun Content(
    emailAddress: String,
    onSendOtp: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .widthIn(max = 488.dp)
            .then(modifier)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.dimens.spacing3XL
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Logo
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.desc_logo)
                )
                Text(
                    stringResource(R.string.head_login),
                    style = MaterialTheme.typeStyle.headlineXLarge,
                    color = MaterialTheme.colors.contentHighlight
                )
            }

            // Input Field with Label
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacingMD),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.lab_email_address),
                    style = MaterialTheme.typeStyle.labelMedium,
                    color = MaterialTheme.colors.contentPrimary
                )
                TextInputField(
                    value = emailAddress,
                    placeholder = stringResource(R.string.placeholder_email_address),
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    errorMessage = errorMessage,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Send
                    )

                    // FIXME: Change keyboard action
                )
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing2XL))
        Button(
            label = stringResource(R.string.act_send_otp),
            onClick = {
                onSendOtp()
                // TODO()

            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ContentInputsOnly( // New Composable
    emailAddress: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    // This is the inner column of your original Content function
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.spacing3XL
        ),
        modifier = modifier.fillMaxWidth() // Use the modifier passed in
    ) {
        // Header
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.desc_logo)
            )
            Text(
                stringResource(R.string.head_login),
                style = MaterialTheme.typeStyle.headlineXLarge,
                color = MaterialTheme.colors.contentHighlight
            )
        }

        // Input Field with Label
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacingMD),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.lab_email_address),
                style = MaterialTheme.typeStyle.labelMedium,
                color = MaterialTheme.colors.contentPrimary
            )
            TextInputField(
                value = emailAddress,
                placeholder = stringResource(R.string.placeholder_email_address),
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                errorMessage = errorMessage,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Send
                )
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LittleLemonTheme {
        LoginScreen({})
    }
}