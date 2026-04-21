package com.littlelemon.application.home.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.AddressPicker
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.home.HomeTestTags

@Composable
fun NavigationSheet(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(LittleLemonTheme.colors.secondary)
    ) {
        // Navbar
        Column(
            Modifier
                .widthIn(min = 280.dp, max = 360.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Image(painterResource(R.drawable.logo_full), null, modifier = Modifier.height(48.dp))
            AddressPicker("Some address: Replace")

            NavigationOption.entries.forEach { navigationOption ->
                NavigationItem(navigationOption, true)
            }
        }
    }
}

@Composable
fun NavigationItem(
    navigationOption: NavigationOption,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val colorTransition = updateTransition(selected)
    val currentColor by colorTransition.animateColor { state ->
        when (state) {
            true -> LittleLemonTheme.colors.contentOnColor
            false -> LittleLemonTheme.colors.contentPlaceholder
        }
    }
    val backgroundColor by colorTransition.animateColor { state ->
        when (state) {
            true -> LittleLemonTheme.colors.primaryDark
            false -> LittleLemonTheme.colors.transparent
        }
    }
    Row(
        modifier
            .background(backgroundColor, LittleLemonTheme.shapes.xl)
            .fillMaxWidth()
            .padding(vertical = LittleLemonTheme.dimens.sizeLG),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val label = stringResource(navigationOption.label)
        Crossfade(selected) { state ->
            Image(
                painterResource(if (state) navigationOption.selectedIcon else navigationOption.defaultIcon),
                contentDescription = label,
                colorFilter = ColorFilter.tint(currentColor),
                modifier = Modifier
                    .testTag(HomeTestTags.BOTTOM_NAVIGATION_ICON)
                    .size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(LittleLemonTheme.dimens.sizeSM))
        Text(
            label,
            style = LittleLemonTheme.typography.labelMedium,
            color = currentColor
        )
    }
}


@Preview(widthDp = 1200, heightDp = 1400)
@Composable
private fun NavigationSheetPreview() {
    LittleLemonTheme {
        NavigationSheet()
    }
}