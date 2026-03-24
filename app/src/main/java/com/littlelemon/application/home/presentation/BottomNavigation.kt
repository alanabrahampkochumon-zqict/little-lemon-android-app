package com.littlelemon.application.home.presentation

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.home.HomeTestTags

@Composable
fun BottomNavigation(modifier: Modifier = Modifier) {
    Row(modifier = modifier) { }
}

@Composable
fun BottomNavigationItem(
    @DrawableRes defaultIcon: Int,
    @DrawableRes selectedIcon: Int,
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorTransition = updateTransition(selected)
    val currentColor by colorTransition.animateColor { state ->
        when (state) {
            true -> MaterialTheme.colors.contentAccentSecondary
            false -> MaterialTheme.colors.contentPlaceholder
        }
    }

    Column(
        modifier = modifier.selectable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onSelect,
            role = Role.Tab,
            selected = selected
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (selected) {
            Image(
                painterResource(selectedIcon),
                contentDescription = label,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.contentAccentSecondary),
                modifier = Modifier.testTag(HomeTestTags.BOTTOM_NAVIGATION_ICON_SELECTED)
            )
        } else {
            Image(
                painterResource(defaultIcon),
                contentDescription = label,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.contentPlaceholder),
                modifier = Modifier.testTag(HomeTestTags.BOTTOM_NAVIGATION_ICON_UNSELECTED)
            )
        }
        Text(
            label,
            style = MaterialTheme.typeStyle.bodyXSmall,
            color = currentColor
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun BottomNavigationItemPreview() {
    LittleLemonTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BottomNavigationItem(
                R.drawable.ic_home_outline,
                R.drawable.ic_home_filled,
                "Home",
                true,
                {})
            BottomNavigationItem(
                R.drawable.ic_home_outline,
                R.drawable.ic_home_filled,
                "Home",
                false,
                {})
        }
    }
}

@Preview
@Composable
private fun BottomNavigationPreview() {
    LittleLemonTheme {
        BottomNavigation()
    }
}