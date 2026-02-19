package com.littlelemon.application.auth.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.AuthState
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.TextInputField
import com.littlelemon.application.core.presentation.components.TopNavigationBar
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInformationContent(
    authState: AuthState,
    modifier: Modifier = Modifier,
    isScrollable: Boolean = false,
    onFirstNameChange: (String) -> Unit = {},
    onLastNameChange: (String) -> Unit = {},
    onComplete: () -> Unit = {}
) {

    Column(
        modifier = modifier
    ) {
        TopNavigationBar(
            label = stringResource(R.string.nav_personal_information),
            subText = stringResource(R.string.body_personalization),
            navigationIconDescription = stringResource(R.string.act_back),
            modifier = Modifier.heightIn(max = 48.dp)
        )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeXL))
        Column(modifier = Modifier.padding(MaterialTheme.dimens.sizeLG)) {
            NameInputField(
                label = stringResource(R.string.label_first_name),
                value = authState.firstName,
                placeholder = stringResource(R.string.placeholder_first_name),
                onValueChange = onFirstNameChange
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2XL))
            NameInputField(
                label = stringResource(R.string.label_last_name),
                value = authState.lastName,
                placeholder = stringResource(R.string.placeholder_last_name),
                onValueChange = onLastNameChange
            )
        }

        if (isScrollable) {
            Spacer(Modifier.height(MaterialTheme.dimens.size3XL))
        } else {
            Spacer(Modifier.weight(1f))
        }

        Box(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.sizeXL)) {
            Button(
                stringResource(R.string.act_go),
                onClick = onComplete,
                enabled = authState.enableLetsGoButton
            )
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
            color = MaterialTheme.colors.contentSecondary
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


@Preview(showBackground = true)
@Composable
private fun PersonalizeInformationContentPreview() {
    LittleLemonTheme {
        PersonalInformationContent(AuthState())
    }
}