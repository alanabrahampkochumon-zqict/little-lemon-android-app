package com.littlelemon.application.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@Composable
fun ActionScreen(
    primaryActionLabel: String,
    secondaryActionLabel: String,
    onPrimaryActionClick: () -> Unit,
    onSecondaryActionClick: () -> Unit,
    @DrawableRes illustration: Int,
    heading: String,
    content: String,
    modifier: Modifier = Modifier,
    headingColor: Color = MaterialTheme.colors.contentHighlight,
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DoodleBackground(alpha = 0.1f)

        Column(
            Modifier
                .padding(MaterialTheme.dimens.sizeXL)
                .verticalScroll(rememberScrollState())
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                heading,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typeStyle.headlineLarge.copy(textAlign = TextAlign.Center),
                color = headingColor
            )
            Spacer(Modifier.height(MaterialTheme.dimens.sizeXS))
            Text(
                content,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 320.dp),
                style = MaterialTheme.typeStyle.bodyMedium.copy(textAlign = TextAlign.Center),
                color = MaterialTheme.colors.contentSecondary
            )
            Spacer(Modifier.height(MaterialTheme.dimens.size3XL))
            Image(
                painterResource(illustration),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .widthIn(max = 360.dp)
                    .fillMaxWidth()
                    .testTag(CoreTestTags.ACTION_SCREEN_ILLUSTRATION)
            )
            Spacer(Modifier.height(MaterialTheme.dimens.size4XL))
            Button(primaryActionLabel, onPrimaryActionClick, variant = ButtonVariant.HIGH_CONTRAST)
            Spacer(Modifier.height(MaterialTheme.dimens.sizeXS))
            Button(secondaryActionLabel, onSecondaryActionClick, variant = ButtonVariant.GHOST)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionScreenPreview() {
    LittleLemonTheme {
        ActionScreen(
            "Primary Action",
            "Secondary Action",
            {},
            {},
            R.drawable.illustration_order_success,
            "This is a test heading",
            "this is some test content"
        )
    }
}