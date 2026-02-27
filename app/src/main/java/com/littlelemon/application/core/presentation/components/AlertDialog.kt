package com.littlelemon.application.core.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.utils.toComposeShadow

@Composable
fun AlertDialog(
    dialogTitle: String,
    dialogText: String,
    showDialog: Boolean,
    positiveActionText: String,
    negativeActionText: String,
    modifier: Modifier = Modifier,
    onPositiveAction: () -> Unit = {},
    onNegativeAction: () -> Unit = {},
    dismissable: Boolean = true,
    onDismissDialog: () -> Unit = {},
    positiveActionVariant: ButtonVariant = ButtonVariant.PRIMARY,
    negativeActionVariant: ButtonVariant = ButtonVariant.GHOST,
    content: @Composable () -> Unit = {},
) {

    val screenDensityRatio = LocalDensity.current.density

    val dialogShape = MaterialTheme.shapes.large
    val dropShadow = MaterialTheme.shadows.dropLG
    val firstShadowLayer = dropShadow.firstShadow.toComposeShadow(screenDensityRatio)
    val secondShadowLayer =
        dropShadow.secondShadow?.toComposeShadow(screenDensityRatio) ?: Shadow(0.dp)

    BackHandler(enabled = dismissable, onBack = onDismissDialog)

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .then(
                    if (showDialog) Modifier
                        .blur(MaterialTheme.dimens.sizeLG)
                        .disableTouch() else Modifier
                )
        ) {
            content()
        }
        // Overlay
        AnimatedVisibility(showDialog) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.darkOverlay24)
                    .clickable(enabled = dismissable, onClick = onDismissDialog)
                    .testTag(stringResource(R.string.test_tag_alert_dialog_overlay))
            )
        }
        // Alert Dialog
        AnimatedVisibility(showDialog) {
            Box(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .padding(horizontal = MaterialTheme.dimens.sizeXL)
                    .clip(
                        dialogShape
                    )
                    .dropShadow(dialogShape, secondShadowLayer)
                    .dropShadow(dialogShape, firstShadowLayer)
                    .background(MaterialTheme.colors.primary)
            ) {
                Column(
                    modifier = Modifier,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.size2XL)
                    ) {
                        Text(
                            dialogTitle,
                            style = MaterialTheme.typeStyle.displaySmall,
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colors.contentPrimary
                        )
                        Spacer(Modifier.height(MaterialTheme.dimens.sizeLG))
                        Text(
                            text = dialogText,
                            style = MaterialTheme.typeStyle.bodyMedium,
                            color = MaterialTheme.colors.contentSecondary
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            label = negativeActionText,
                            variant = negativeActionVariant,
                            onClick = onNegativeAction,
                            modifier = Modifier.weight(1f),
                            buttonShape = ButtonShape.Square
                        )
                        Button(
                            label = positiveActionText,
                            variant = positiveActionVariant,
                            onClick = onPositiveAction,
                            modifier = Modifier.weight(1f),
                            buttonShape = ButtonShape.Square
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun AlertDialogPreview() {
    LittleLemonTheme {
        AlertDialog(
            "Sample Dialog Title",
            "This is a sample dialog paragraph that is not quite long.",
            true,
            "Positive Action",
            "Negative Action",
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary)
                    .padding(16.dp)
            ) {
                items(100) {
                    Column(
                        Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Heading $it",
                            style = MaterialTheme.typeStyle.headlineMedium,
                            color = MaterialTheme.colors.contentPrimary,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Long body copy $it",
                            color = MaterialTheme.colors.contentPrimary,
                            style = MaterialTheme.typeStyle.bodyMedium
                        )
                        VerticalDivider()

                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AlertDialogNotShownPreview() {
    LittleLemonTheme {
        AlertDialog(
            "Sample Dialog Title",
            "This is a sample dialog paragraph that is not quite long.",
            false,
            "Positive Action",
            "Negative Action",
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary)
                    .padding(16.dp)
            ) {
                items(100) {
                    Column(
                        Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Heading $it",
                            style = MaterialTheme.typeStyle.headlineMedium,
                            color = MaterialTheme.colors.contentPrimary,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Long body copy $it",
                            color = MaterialTheme.colors.contentPrimary,
                            style = MaterialTheme.typeStyle.bodyMedium
                        )
                        VerticalDivider()

                    }
                }
            }
        }
    }
}