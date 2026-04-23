package com.littlelemon.application.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow

@Composable
fun BottomSheet(
    showBottomSheet: Boolean,
    modifier: Modifier = Modifier,
    dismissable: Boolean = true,
    onDismiss: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {},
) {
    val bottomSheetShape = LittleLemonTheme.shapes.xl.copy(
        bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)
    )

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(showBottomSheet, enter = fadeIn(), exit = fadeOut()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LittleLemonTheme.colors.darkOverlay24)
                    .testTag(CoreTestTags.BOTTOM_SHEET_BACKGROUND)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { if (dismissable) onDismiss() })
        }
        AnimatedVisibility(
            showBottomSheet,
            enter = slideInVertically { it } + fadeIn(initialAlpha = 0.8f),
            exit = slideOutVertically { it }) {
            Box(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .heightIn(min = 400.dp, max = 900.dp)
                    .applyShadow(bottomSheetShape, LittleLemonTheme.shadows.upperLG)
                    .background(LittleLemonTheme.colors.primary, shape = bottomSheetShape)
                    .fillMaxWidth()
                    .padding(LittleLemonTheme.dimens.size2XL)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {}
                    .testTag(CoreTestTags.BOTTOM_SHEET)
            ) {
                content()
            }
        }
    }
}

@Preview(widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
private fun BottomSheetPreview() {
    var showBottomSheet by remember { mutableStateOf(true) }
    LittleLemonTheme {
        Column(Modifier.fillMaxSize()) {
            Text("Hello, from content!")
        }
        BottomSheet(
            showBottomSheet = showBottomSheet, onDismiss = { showBottomSheet = !showBottomSheet }) {
            Text("Text")
        }
    }
}