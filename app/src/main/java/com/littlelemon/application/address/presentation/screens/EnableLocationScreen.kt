package com.littlelemon.application.address.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonSize
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.components.DoodleBackground
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@Composable
fun EnableLocationScreen(modifier: Modifier = Modifier) {
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
                    onClick = {}, // TODO:
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.PRIMARY,
                    size = ButtonSize.Medium
                )
                Button(
                    label = stringResource(R.string.act_enter_location_manually),
                    onClick = {}, // TODO:
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.SECONDARY,
                    size = ButtonSize.Medium
                )
            }

        }
    }

}

@Preview
@Composable
private fun EnableLocationScreenPreview() {
    LittleLemonTheme {
        EnableLocationScreen()
    }
}