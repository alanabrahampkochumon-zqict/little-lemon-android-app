package com.littlelemon.application.auth.presentation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.components.Button
import com.littlelemon.application.core.presentation.designsystem.components.TextInputField
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xLarge

@Composable
fun LoginScreen(onSendOtp: () -> Unit, modifier: Modifier = Modifier) {


    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colors.actionHover,
            MaterialTheme.colors.action,
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    val illustrationAspectRatio = (375.0 / 402.0).toFloat()

    var emailAddress by remember {
        mutableStateOf("")
    }
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    fun sendOTP() {} // TODO: Replace with VM action

    // Forcing status bar content to be dark
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            // Force Icons to be BLACK (True) because the image is Light
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
//        }
//    }

    // Handle Orientation
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    val windowInsets = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
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
        contentWindowInsets = WindowInsets.systemBars.only(
            WindowInsetsSides.Top
        ).add(WindowInsets.displayCutout)
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(backgroundGradient)
        ) {

            Image(
                painter = painterResource(R.drawable.doodles),
                contentDescription = stringResource(R.string.desc_logo),
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.contentInverse),
                alpha = 0.64f
            )
        }
        // Content
        Column(Modifier.padding(innerPadding)) {
            Box(modifier = Modifier.weight(1f)) { //FIXME: Change from fixed height

            }
            Column(
                Modifier
                    .background(
                        MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.xLarge.copy(
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                    )
                    .height(640.dp)
                    .fillMaxHeight()
                    .padding(
                        paddingValues = PaddingValues(
                            top = MaterialTheme.dimens.spacing3XL,
                            bottom = MaterialTheme.dimens.spacing4XL,
                            start = MaterialTheme.dimens.spacing2XL,
                            end = MaterialTheme.dimens.spacing2XL
                        )
                    ),
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(48.dp),
                    painter = painterResource(R.drawable.logo_full),
                    contentDescription = null
                )
                Spacer(Modifier.height(MaterialTheme.dimens.spacing2XL))
                Text(
                    stringResource(R.string.heading_login),
                    style = MaterialTheme.typeStyle.headlineLarge,
                    color = MaterialTheme.colors.contentSecondary
                )
                Spacer(Modifier.height(MaterialTheme.dimens.spacingLG))

                Column(
                    Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.spacingSM
                    )
                ) {
                    TextInputField(
                        stringResource(R.string.placeholder_email_address),
                        value = emailAddress
                    )
                    Text(
                        stringResource(R.string.body_email_description),
                        style = MaterialTheme.typeStyle.bodySmall,
                        color = MaterialTheme.colors.contentTertiary
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(
                    stringResource(R.string.act_send_otp),
                    ::sendOTP,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
//
//        when (orientation) {
//            Configuration.ORIENTATION_PORTRAIT -> {
//                Column(
//                    modifier = Modifier
//                        .padding(innerPadding)
//                        .fillMaxSize()
//                        .imePadding()
//                        .verticalScroll(rememberScrollState())
//                        .height(IntrinsicSize.Max),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    // Hero Image
//                    Image(
//                        painter = painterResource(R.drawable.illustration_people_dining),
//                        contentDescription = null,
//                        Modifier
//                            .fillMaxWidth()
//                            .aspectRatio(illustrationAspectRatio),
//                        contentScale = ContentScale.FillBounds
//                    )
//
//                    Content(
//                        emailAddress,
//                        onEmailChange = { newEmail ->
//                            emailAddress = newEmail
//                        },
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(
//                                start = MaterialTheme.dimens.spacingLG,
//                                end = MaterialTheme.dimens.spacingLG,
//                                top = MaterialTheme.dimens.spacingXL,
//                                bottom = MaterialTheme.dimens.spacingXL,
//                            ),
//                        errorMessage = errorMessage,
//                        onButtonClick = onSendOtp
//                    )
//                }
//            }
//
//            else -> {
//                Row(
//                    modifier = Modifier
//                        .padding(innerPadding)
//                        .fillMaxSize(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacingLG)
//                ) {
//                    // Hero Image
//                    Image(
//                        painter = painterResource(R.drawable.illustration_people_dining),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxHeight(),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(
//                                end = MaterialTheme.dimens.spacing2XL
//                            )
//                            .imePadding()
//                            .widthIn(max = 488.dp)
//                            .fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//
//
//                        Content(
//                            emailAddress,
//                            onEmailChange = { newEmail ->
//                                emailAddress = newEmail
//                            },
//                            modifier = Modifier.verticalScroll(rememberScrollState()),
//                            errorMessage = errorMessage,
//                            onButtonClick = onSendOtp
//                        )
//                    }
//                }
//            }
//        }

    }
}

@Composable
private fun Content(
    emailAddress: String,
    onButtonClick: () -> Unit,
    onEmailChange: (String) -> Unit,
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
                    stringResource(R.string.heading_login),
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
                    stringResource(R.string.label_email_address),
                    style = MaterialTheme.typeStyle.labelMedium,
                    color = MaterialTheme.colors.contentPrimary
                )
                TextInputField(
                    value = emailAddress,
                    placeholder = stringResource(R.string.placeholder_email_address),
                    onValueChange = onEmailChange,
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
                onButtonClick()
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
                stringResource(R.string.heading_login),
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
                stringResource(R.string.label_email_address),
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