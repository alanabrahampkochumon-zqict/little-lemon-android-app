package com.littlelemon.application.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle


@Composable
fun BasicBadge(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colors.contentOnAction
) {

    val backgroundColor =
        animateColorAsState(if (selected) MaterialTheme.colors.action else MaterialTheme.colors.secondary)
    val borderColor =
        animateColorAsState(if (selected) MaterialTheme.colors.transparent else MaterialTheme.colors.action)
    val shape =MaterialTheme.shapes.large
    val density = LocalDensity.current.density
    val strokeWidth = 1 * density
    val stroke = remember { Stroke(strokeWidth, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f * density, 4f * density), 0f), cap = StrokeCap.Round) }

    Row(
        modifier = modifier
            .selectable(selected = selected, onClick = onSelect)
            .background(
                backgroundColor.value,
                shape = shape
            ).drawBehind {
                drawRoundRect(borderColor.value, style = stroke, cornerRadius = CornerRadius(999f, 999f))
            }
            .padding(
                vertical = MaterialTheme.dimens.sizeSM, horizontal = MaterialTheme.dimens.sizeXL
            ).animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeMD),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(selected, enter = scaleIn(), exit = scaleOut()) {
            Image(
                painterResource(R.drawable.ic_check),
                null,
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(contentColor)
            )
        }

        Text(label, style = MaterialTheme.typeStyle.labelMedium, color = contentColor)
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