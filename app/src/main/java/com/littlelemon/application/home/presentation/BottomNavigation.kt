package com.littlelemon.application.home.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@Composable
fun BottomNavigation(modifier: Modifier = Modifier) {
    Row(modifier = modifier) { }
}

@Composable
fun BottomNavigationItem(
    @DrawableRes icon: Int,
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor =
        if (selected) MaterialTheme.colors.contentAccentSecondary else MaterialTheme.colors.contentPlaceholder
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
        Image(
            painterResource(icon),
            contentDescription = label,
            colorFilter = ColorFilter.tint(contentColor)
        )
        Text(
            label,
            style = MaterialTheme.typeStyle.bodyXSmall,
            color = contentColor
        )
    }
}


@Preview
@Composable
private fun BottomNavigationPreview() {
    LittleLemonTheme {
        BottomNavigation()
    }
}