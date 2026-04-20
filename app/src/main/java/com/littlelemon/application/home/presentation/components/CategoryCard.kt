package com.littlelemon.application.home.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow
import com.littlelemon.application.home.HomeTestTags

@Composable
fun CategoryCard(
    label: String,
    selected: Boolean,
    onSelectionChange: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    val contentColor = if (!enabled) LittleLemonTheme.colors.contentDisabled
    else if (selected) LittleLemonTheme.colors.contentHighlight
    else LittleLemonTheme.colors.contentSecondary

    val backgroundColor = if (enabled) LittleLemonTheme.colors.primary
    else LittleLemonTheme.colors.disabled

    val shape = LittleLemonTheme.shapes.xs
    val shadow = LittleLemonTheme.shadows.dropXS

    Row(
        modifier = modifier
            .then(if (enabled && !selected) Modifier.applyShadow(shape, shadow) else Modifier)
            .background(backgroundColor, shape)
            .then(if (selected) Modifier.border(1.dp, contentColor, shape) else Modifier)
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.DropdownList,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelectionChange
            )
            .padding(
                horizontal = LittleLemonTheme.dimens.size2XL,
                vertical = LittleLemonTheme.dimens.sizeMD
            )
            .animateContentSize(), verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(selected) {
            Row {
                Image(
                    painterResource(R.drawable.ic_checkcircle_filled),
                    null,
                    modifier = Modifier
                        .size(
                            LittleLemonTheme.dimens.sizeXL
                        )
                        .testTag(HomeTestTags.CATEGORY_CARD_CHECK_MARK),
                    colorFilter = ColorFilter.tint(contentColor)
                )
                Spacer(modifier = Modifier.width(LittleLemonTheme.dimens.sizeMD))
            }
        }
        Text(label, style = LittleLemonTheme.typography.labelSmall, color = contentColor)
    }
}


@Preview(showBackground = true)
@Composable
private fun CategoryCardPreview() {
    LittleLemonTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            CategoryCard("Category", true, {})
            CategoryCard("Category", false, {})
            CategoryCard("Category", true, {}, enabled = false)
            CategoryCard("Category", false, {}, enabled = false)
        }

    }
}