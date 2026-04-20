package com.littlelemon.application.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme


@Composable
fun BasicBadge(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = LittleLemonTheme.colors.contentOnAction
) {

    val backgroundColor =
        animateColorAsState(if (selected) LittleLemonTheme.colors.action else LittleLemonTheme.colors.secondary)
    val borderColor =
        animateColorAsState(if (selected) LittleLemonTheme.colors.transparent else LittleLemonTheme.colors.action)
    val shape = LittleLemonTheme.shapes.xl
    val density = LocalDensity.current.density
    val strokeWidth = 1 * density
    val stroke = remember {
        Stroke(
            strokeWidth,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f * density, 4f * density), 0f),
            cap = StrokeCap.Round
        )
    }

    Row(
        modifier = modifier
            .selectable(selected = selected, onClick = onSelect)
            .background(
                backgroundColor.value,
                shape = shape
            )
            .drawBehind {
                drawRoundRect(
                    borderColor.value,
                    style = stroke,
                    cornerRadius = CornerRadius(999f, 999f)
                )
            }
            .padding(
                vertical = LittleLemonTheme.dimens.sizeSM,
                horizontal = LittleLemonTheme.dimens.sizeXL
            )
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(LittleLemonTheme.dimens.sizeMD),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(selected, enter = scaleIn(), exit = scaleOut()) {
            Image(
                painterResource(R.drawable.ic_check),
                null,
                modifier = Modifier
                    .size(16.dp)
                    .testTag(
                        CoreTestTags.BADGE_ICON
                    ),
                colorFilter = ColorFilter.tint(contentColor)
            )
        }

        Text(label, style = LittleLemonTheme.typography.labelMedium, color = contentColor)
    }

}


@Preview(showBackground = true)
@Composable
private fun BadgePreview() {
    LittleLemonTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BasicBadge("Badge", false, {})
            BasicBadge("Badge", true, {})
        }
    }
}