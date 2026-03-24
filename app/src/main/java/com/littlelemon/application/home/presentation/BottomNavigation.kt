package com.littlelemon.application.home.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.home.HomeTestTags


// TODO: Add ripple
enum class NavigationOption(
    @field:DrawableRes val selectedIcon: Int,
    @field:DrawableRes val defaultIcon: Int,
    @field:StringRes val label: Int,
) {
    HOME(R.drawable.ic_home_filled, R.drawable.ic_home_outline, R.string.navigation_home),
    MENU(R.drawable.ic_hat_filled, R.drawable.ic_hat_outline, R.string.navigation_menu),
    ORDER(R.drawable.ic_bill_filled, R.drawable.ic_bill_outline, R.string.navigation_order),
    CART(R.drawable.ic_bag_filled, R.drawable.ic_bag_outline, R.string.navigation_cart),
    PROFILE(
        R.drawable.ic_profile_filled,
        R.drawable.ic_profile_outline,
        R.string.navigation_profile
    )
}

@Composable
fun BottomNavigation(
    onNavigationClick: (item: NavigationOption) -> Unit,
    modifier: Modifier = Modifier,
    selected: NavigationOption = NavigationOption.HOME,
) {
    Row(
        modifier = modifier
            .background(
                MaterialTheme.colors.primary
            )
            .padding(
                horizontal = MaterialTheme.dimens.sizeXL,
                vertical = MaterialTheme.dimens.sizeMD
            )
            .navigationBarsPadding()
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        NavigationOption.entries.forEachIndexed { index, option ->
            BottomNavigationItem(
                option.defaultIcon,
                option.selectedIcon,
                label = stringResource(option.label),
                selected = option == selected,
                onSelect = { onNavigationClick(option) },
            )
            if (index < NavigationOption.entries.size - 1)
                Spacer(modifier.width(MaterialTheme.dimens.sizeXL))
        }
    }
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
        modifier = modifier
            .size(48.dp)
            .selectable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect,
                role = Role.Tab,
                selected = selected
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Crossfade(selected) { state ->
            Image(
                painterResource(if (state) selectedIcon else defaultIcon),
                contentDescription = label,
                colorFilter = ColorFilter.tint(currentColor),
                modifier = Modifier.testTag(HomeTestTags.BOTTOM_NAVIGATION_ICON)
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeXS))
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
        BottomNavigation({})
    }
}