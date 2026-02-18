package com.littlelemon.application.auth.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.components.Button
import com.littlelemon.application.core.presentation.designsystem.components.TextInputField
import com.littlelemon.application.core.presentation.designsystem.components.TopNavigationBar
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInformationContent(authState: AuthState) {
    var firstName by remember {
        mutableStateOf("")
    }

    fun firstNameChange(newName: String) {
        firstName = newName
    }/*FIXME: End Change to VM */

    //FIXME: Add Scrollable Navbar

    // Configuration and Orientation
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    orientation == Configuration.ORIENTATION_PORTRAIT
    val maxWidth = 488.dp
    val alignment = if (configuration.screenWidthDp.dp > maxWidth) {
        Alignment.CenterHorizontally
    } else {
        Alignment.Start
    }



    Scaffold(
        topBar = {
            TopNavigationBar(
                label = stringResource(R.string.nav_personal_information),
                navigationIcon = R.drawable.ic_caretleft,
                navigationIconDescription = stringResource(R.string.act_back, "Verification Screen")
            )
        }, contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout),
        containerColor = MaterialTheme.colors.primary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
//            val height = this.maxHeight
            Column(
                modifier = Modifier
                    .widthIn(max = maxWidth)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = MaterialTheme.dimens.size2XL,
                        start = MaterialTheme.dimens.sizeXL,
                        end = MaterialTheme.dimens.sizeXL,
                        bottom = MaterialTheme.dimens.sizeXL,
                    ),
                horizontalAlignment = alignment
            ) {

                Text(
                    text = stringResource(R.string.heading_personalize),
                    style = MaterialTheme.typeStyle.headlineLarge,
                    color = MaterialTheme.colors.contentHighlight
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeXL))
                NameInputField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.label_first_name),
                    placeholder = stringResource(R.string.placeholder_first_name),
                    value = firstName,
                    onValueChange = { firstNameChange(it) }) // FIXME: Replace
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeXL))
                NameInputField(
                    label = stringResource(R.string.label_last_name),
                    placeholder = stringResource(R.string.placeholder_last_name),
                    value = firstName,
                    onValueChange = { firstNameChange(it) }) // FIXME: Replace
                Spacer(
                    modifier = Modifier
                        .height(MaterialTheme.dimens.size2XL)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    label = stringResource(R.string.act_go),
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .widthIn(max = maxWidth)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun NameInputField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typeStyle.labelMedium,
            color = MaterialTheme.colors.contentPrimary
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeMD))
        TextInputField(
            placeholder = placeholder,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview
@Composable
private fun PersonalInformationContentPreview() {
    LittleLemonTheme {
        PersonalInformationContent(AuthState())
    }
}