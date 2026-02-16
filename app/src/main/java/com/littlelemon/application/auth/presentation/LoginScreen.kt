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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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

    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    val screenDensityRatio = context.resources.displayMetrics.density
    val screenHeight = configuration.screenHeightDp.dp

    val floatingHeight = 700.dp
    val floatingWidth = 600.dp // Compact Width

    val floating = screenHeight > floatingHeight && screenWidth > floatingWidth

    val maxHeight = if (floating) 640.dp else Dp(0.85f * screenHeight.value)

    val screenOrientation = configuration.orientation
    val isLandscape = screenOrientation == Configuration.ORIENTATION_LANDSCAPE

    val isScrollable = isLandscape && !floating

    var emailAddress by remember {
        mutableStateOf("")
    }
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val scrollState = rememberScrollState()

    var enabled by remember {
        mutableStateOf(false)
    }

    // Used to adjust the spacing cause by font rendering of Markazi text.
    val fontOffset = 6.dp

    fun sendOTP() {} // TODO: Replace with VM action
    // Handle Orientation
    val orientation = configuration.orientation

    val cardShape = MaterialTheme.shapes.xLarge.copy(
        bottomStart = if (floating) MaterialTheme.shapes.large.bottomStart else CornerSize(0.dp),
        bottomEnd = if (floating) MaterialTheme.shapes.large.bottomEnd else CornerSize(0.dp)
    )

    val scrollState2 = rememberScrollState()
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars.only(
            WindowInsetsSides.Top
        ).add(WindowInsets.displayCutout).add(WindowInsets.ime)
    ) { innerPadding ->

        // Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colors.secondary)
        ) {

            Image(
                painter = painterResource(R.drawable.doodles),
                contentDescription = stringResource(R.string.desc_logo),
                Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.contentHighlight),
                alpha = 0.24f
            )
        }

        // Content
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (floating) Arrangement.Center else Arrangement.Bottom
        ) {
            Column(
                Modifier
                    .widthIn(max = 480.dp)
                    .dropShadow(
                        shape = cardShape,
                        shadow = MaterialTheme.shadows.upperXL.firstShadow.toComposeShadow(
                            screenDensityRatio
                        )
                    )
                    .dropShadow(
                        shape = cardShape,
                        shadow = MaterialTheme.shadows.upperXL.secondShadow?.toComposeShadow(
                            screenDensityRatio
                        )
                            ?: Shadow(radius = 0.dp)
                    )
                    .background(
                        MaterialTheme.colors.primary,
                        shape = cardShape
                    )
                    .padding(
                        paddingValues = PaddingValues(
                            top = MaterialTheme.dimens.size2XL,
                            end = MaterialTheme.dimens.size2XL,
                            start = MaterialTheme.dimens.size2XL,
                            bottom = if (floating) MaterialTheme.dimens.size2XL else MaterialTheme.dimens.size4XL,
                        )
                    )
                    .then(
                        if (isScrollable) Modifier
                            .fillMaxHeight()
                            .verticalScroll(scrollState)
                            .imePadding()
                        else Modifier
                            .heightIn(max = maxHeight)
                            .fillMaxHeight()
                    ),
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(48.dp),
                    painter = painterResource(R.drawable.logo_full),
                    contentDescription = null
                )
                Spacer(Modifier.height(MaterialTheme.dimens.size3XL - fontOffset))
                Text(
                    stringResource(R.string.heading_login),
                    style = MaterialTheme.typeStyle.displaySmall,
                    color = MaterialTheme.colors.contentPrimary,
                )
                Spacer(Modifier.height(MaterialTheme.dimens.sizeLG - fontOffset))

                Column(
                    Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.sizeSM
                    )
                ) {
                    TextInputField(
                        stringResource(R.string.placeholder_email_address),
                        value = emailAddress
                    )
                    Text(
                        stringResource(R.string.body_email_description),
                        style = MaterialTheme.typeStyle.bodyXSmall,
                        color = MaterialTheme.colors.contentTertiary
                    )
                }
                if (isScrollable) {
                    Spacer(Modifier.height(MaterialTheme.dimens.size3XL))
                } else {
                    Spacer(Modifier.weight(1f))
                }
                Button(
                    stringResource(R.string.act_send_otp),
                    ::sendOTP,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled
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