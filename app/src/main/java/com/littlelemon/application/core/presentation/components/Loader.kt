package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.utils.toComposeShadow


@Composable
fun Loader(
    modifier: Modifier = Modifier,
    screenDensityRatio: Float = 2f,
    showLoader: Boolean = false,
    loaderContent: @Composable ColumnScope.() -> Unit = {},
    screenContent: @Composable () -> Unit = {}
) {

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (showLoader) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.darkOverlay24)
                    .blur(MaterialTheme.dimens.sizeMD)
                    .fillMaxSize()
            ) {
                screenContent()
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.sizeXL
                ),
                modifier = Modifier
                    .dropShadow(
                        MaterialTheme.shapes.medium,
                        MaterialTheme.shadows.dropXL.firstShadow.toComposeShadow(screenDensityRatio)
                    )
                    .dropShadow(
                        MaterialTheme.shapes.medium,
                        MaterialTheme.shadows.dropXL.secondShadow?.toComposeShadow(
                            screenDensityRatio
                        )
                            ?: Shadow(0.dp)
                    )
                    .background(
                        MaterialTheme.colors.primary, shape = MaterialTheme.shapes.medium
                    )
                    .padding(MaterialTheme.dimens.sizeXL)
            ) {
                CircularProgressIndicator(
                    trackColor = MaterialTheme.colors.transparent,
                    color = MaterialTheme.colors.action,
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(MaterialTheme.dimens.size4XL)
                )
                loaderContent()
            }
        } else {
            screenContent()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LoaderPreview() {
    LittleLemonTheme {
        Box(Modifier.padding()) {
            // Sample content
            Loader(
                showLoader = true,
                loaderContent = { Text("This is some sample loading content") }) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Very long paragraphs of text")
                    Text("Very long paragraphs of text")
                    Text("Very long paragraphs of text")
                    Text("Very long paragraphs of text")
                    Text("Very long paragraphs of text")
                    Text("Very long paragraphs of text")
                    Text("Very long paragraphs of text")
                    Text("Very long paragraphs of text")
                    Box(
                        Modifier
                            .size(200.dp)
                            .background(MaterialTheme.colors.action)
                    )
                }
            }

        }
    }
}