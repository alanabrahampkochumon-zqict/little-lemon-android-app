package com.littlelemon.application.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun Checkbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
    label: String? = null,
    onCheckedChange: (Boolean) -> Unit = {}
) {

    val interactionSource = remember { MutableInteractionSource() }
    val shape = LittleLemonTheme.shapes.xs
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            LittleLemonTheme.dimens.sizeMD
        ),
        modifier = modifier
            .heightIn(min = 48.dp)
            .widthIn(min = 48.dp)
            .toggleable(
                role = Role.Checkbox,
                indication = null,
                interactionSource = interactionSource,
                value = checked
            ) { onCheckedChange(!checked) }) {
        Row(
            modifier = Modifier
                .size(28.dp)
                .background(
                    LittleLemonTheme.colors.primary,
                    shape = shape
                )
                .border(BorderStroke(2.dp, LittleLemonTheme.colors.contentHighlight), shape)
                .clip(shape)
                .indication(interactionSource = interactionSource, ripple()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                checked,
                enter = fadeIn(
                    tween(
                        durationMillis = 100,
                        easing = LinearOutSlowInEasing
                    )
                ) + scaleIn(initialScale = 0.5f),
                exit = fadeOut(
                    tween(
                        durationMillis = 100,
                        easing = LinearOutSlowInEasing
                    )
                ) + scaleOut()
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        LittleLemonTheme.colors.contentHighlight
                    ),
                    modifier = Modifier.testTag(stringResource(R.string.test_tag_checkbox_icon))
                )
            }
        }
        label?.let {
            Text(
                text = it,
                style = LittleLemonTheme.typography.labelMedium,
                color = LittleLemonTheme.colors.contentSecondary
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun CheckboxPreview() {
    LittleLemonTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            val checkedStated = remember { mutableStateOf(false) }
            Checkbox(true)
            Checkbox(false)
            Checkbox(
                checkedStated.value,
                label = "Label",
                onCheckedChange = { checkedStated.value = it })
        }
    }
}