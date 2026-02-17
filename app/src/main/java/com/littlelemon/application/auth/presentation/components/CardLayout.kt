package com.littlelemon.application.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.xLarge
import com.littlelemon.application.core.presentation.utils.toComposeShadow

@Composable
fun CardLayout(
    modifier: Modifier = Modifier,
    isFloating: Boolean = false,
    isScrollable: Boolean = false,
    maxHeight: Dp = 700.dp,
    screenDensityRatio: Float = 2.0f,
    content: @Composable () -> Unit = {}
) {

    val scrollState = rememberScrollState()

    val cardShape = MaterialTheme.shapes.xLarge.copy(
        bottomStart = if (isFloating) MaterialTheme.shapes.large.bottomStart else CornerSize(0.dp),
        bottomEnd = if (isFloating) MaterialTheme.shapes.large.bottomEnd else CornerSize(0.dp)
    )
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isFloating) Arrangement.Center else Arrangement.Bottom
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
                        top = MaterialTheme.dimens.sizeXL,
                        start = MaterialTheme.dimens.sizeMD,
                        end = MaterialTheme.dimens.sizeMD,
                        bottom = if (isFloating) MaterialTheme.dimens.size2XL else MaterialTheme.dimens.size4XL,
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