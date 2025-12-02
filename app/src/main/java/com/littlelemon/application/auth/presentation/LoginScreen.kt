package com.littlelemon.application.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.components.Button
import com.littlelemon.application.core.presentation.designsystem.components.TextInputField
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle


//FIXME: Screen jumping on keyboard appear
//FIXME: Button maxwidth: 320dp
//FIXME: Orientation Support Add
//FIXME: Statusbar color
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val illustrationAspectRatio = (375.0 / 402.0).toFloat()

    var emailAddress by remember {
        mutableStateOf("")
    }
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(modifier = modifier, containerColor = MaterialTheme.colors.primary) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image
            Image(
                painter = painterResource(R.drawable.illustration_people_dining),
                contentDescription = null,
                modifier
                    .fillMaxWidth()
                    .aspectRatio(illustrationAspectRatio),
                contentScale = ContentScale.FillBounds
            )


            // Content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing2XL),
                modifier = Modifier.padding(
                    start = MaterialTheme.dimens.spacingLG,
                    end = MaterialTheme.dimens.spacingLG,
                    top = MaterialTheme.dimens.spacingXL
                )
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
                            .widthIn(max = 320.dp)
                    ) {
                        Text(
                            stringResource(R.string.lab_email_address),
                            style = MaterialTheme.typeStyle.labelMedium,
                            color = MaterialTheme.colors.contentPrimary
                        )
                        TextInputField(
                            value = emailAddress,
                            placeholder = stringResource(R.string.placeholder_email_address),
                            onValueChange = { newEmail ->
                                emailAddress = newEmail
                            }, modifier = Modifier.fillMaxWidth(),
                            errorMessage = errorMessage
                        )
                    }
                }

                Button(
                    label = stringResource(R.string.act_send_otp),
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom)))
            // Add a little extra space for aesthetics
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacingLG))

        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LittleLemonTheme {
        LoginScreen()
    }
}