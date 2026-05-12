package com.littlelemon.application.home.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.core.presentation.components.AddressPicker
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow
import com.littlelemon.application.home.HomeTestTags

@Composable
fun NavigationSheet(content: @Composable () -> Unit, modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableStateOf(NavigationOption.HOME) } // TODO: Hoist
    val contentCardShape = LittleLemonTheme.shapes.xl.copy(
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp)
    )
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(LittleLemonTheme.colors.secondary),
    ) {
        // Navbar
        Column(
            Modifier
                .widthIn(min = 280.dp, max = 360.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(LittleLemonTheme.dimens.sizeXL),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.logo_full),
                null,
                modifier = Modifier
                    .height(48.dp)
                    .testTag(
                        CoreTestTags.LOGO
                    )
            )
            Spacer(Modifier.height(LittleLemonTheme.dimens.size2XL))
            AddressPicker("Some address: Replace", onAddressChange = {}, elevated = true)
            Spacer(Modifier.height(LittleLemonTheme.dimens.size3XL))
            NavigationOption.entries.forEach { navigationOption ->
                NavigationItem(
                    navigationOption,
                    selectedItem == navigationOption,
                    { selectedItem = it })
                Spacer(Modifier.height(LittleLemonTheme.dimens.sizeSM))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .applyShadow(contentCardShape, LittleLemonTheme.shadows.dropLG)
                .background(
                    LittleLemonTheme.colors.primary,
                    shape = contentCardShape
                )
                .padding(horizontal = LittleLemonTheme.dimens.sizeSM)
        ) {
            content()
        }
    }
}

// TODO: Add test
@Composable
fun NavigationItem(
    navigationOption: NavigationOption,
    selected: Boolean,
    onSelected: (NavigationOption) -> Unit,
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
            .padding(vertical = LittleLemonTheme.dimens.sizeLG)
            .selectable(selected, onClick = { onSelected(navigationOption) }),
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
        NavigationSheet({
            Text("Hello, world")
        })
    }
}