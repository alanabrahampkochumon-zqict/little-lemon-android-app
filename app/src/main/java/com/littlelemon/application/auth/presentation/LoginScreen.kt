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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.components.Button
import com.littlelemon.application.core.presentation.designsystem.components.TextInputField
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xLarge
import com.littlelemon.application.core.presentation.utils.toComposeShadow

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
        modifier = modifier
            .fillMaxSize()
            .background(
                Color.Black
            ),
        containerColor = MaterialTheme.colors.primary,
        contentWindowInsets = WindowInsets.systemBars.only(
            WindowInsetsSides.Top
        ).add(WindowInsets.displayCutout)
    ) { innerPadding ->

        // Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colors.primaryDark)
        ) {

            Image(
                painter = painterResource(R.drawable.doodles),
                contentDescription = stringResource(R.string.desc_logo),
                Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.contentInverse),
                alpha = 0.48f
            )
        }

        // Content
        Column(
            Modifier
                .padding(innerPadding)
        ) {
            Box(modifier = Modifier.weight(0.2f)) // Padding Top
            Column(
                Modifier
                    .dropShadow(
                        shape = RoundedCornerShape(20.dp),
                        shadow = MaterialTheme.shadows.shadowUpperMD.firstShadow.toComposeShadow()
                    )
                    .dropShadow(
                        shape = RoundedCornerShape(20.dp),
                        shadow = MaterialTheme.shadows.shadowUpperMD.secondShadow?.toComposeShadow()
                            ?: Shadow(radius = 0.dp)
                    )
                    .background(
                        MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.xLarge.copy(
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                    )
                    // FIXME: Drop shadow causing screen to be less white
                    .weight(0.8f)
                    .fillMaxHeight()
                    .padding(
                        paddingValues = PaddingValues(
                            top = MaterialTheme.dimens.size2XL,
                            end = MaterialTheme.dimens.size2XL,
                            start = MaterialTheme.dimens.size2XL,
                            bottom = MaterialTheme.dimens.size4XL,
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
                Spacer(Modifier.height(MaterialTheme.dimens.size3XL))
                Text(
                    stringResource(R.string.heading_login),
                    style = MaterialTheme.typeStyle.displaySmall,
                    color = MaterialTheme.colors.contentPrimary
                )
                Spacer(Modifier.height(MaterialTheme.dimens.sizeLG))

                Column(
                    Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.sizeSM
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
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LittleLemonTheme {
        LoginScreen({})
    }
}