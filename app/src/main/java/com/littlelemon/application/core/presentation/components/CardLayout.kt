package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow

@Composable
fun CardLayout(
    modifier: Modifier = Modifier,
    isFloating: Boolean = false,
    isScrollable: Boolean = false,
    maxHeight: Dp = 700.dp,
    maxWidth: Dp = 700.dp,
    content: @Composable () -> Unit = {}
) {

    val scrollState = rememberScrollState()

    val cardShadow =
        if (isFloating) LittleLemonTheme.shadows.dropXL else LittleLemonTheme.shadows.upperMD
    val cardShape =
        if (isFloating) LittleLemonTheme.shapes.lg else LittleLemonTheme.shapes.attachedCardShape
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isFloating) Arrangement.Center else Arrangement.Bottom
    ) {
        Column(
            Modifier
                .widthIn(max = maxWidth)
                .fillMaxWidth()
                .applyShadow(cardShape, cardShadow)
                .background(
                    LittleLemonTheme.colors.primary,
                    shape = cardShape
                )
                .padding(
                    paddingValues = PaddingValues(
                        top = LittleLemonTheme.dimens.sizeXL,
                        bottom = if (isFloating) LittleLemonTheme.dimens.size2XL else LittleLemonTheme.dimens.size4XL,
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
            content()
        }
    }
}

@Preview(backgroundColor = 0xf5f5f6, showBackground = true)
@Composable
private fun CardLayoutPreview() {
    LittleLemonTheme {
        CardLayout(modifier = Modifier.fillMaxWidth()) {
            Text("Hello from card view!")
            Text("Hello from card view!")
            Text("Hello from card view!")
            Text("Hello from card view!")
        }
    }
}

@Preview(backgroundColor = 0xf5f5f6, showBackground = true, widthDp = 1000, heightDp = 1000)
@Composable
private fun FloatingCardLayoutPreview() {
    LittleLemonTheme {
        CardLayout(modifier = Modifier.fillMaxWidth(), isFloating = true) {
            Text("Hello from card view!")
            Text("Hello from card view!")
            Text("Hello from card view!")
            Text("Hello from card view!")
        }
    }
}