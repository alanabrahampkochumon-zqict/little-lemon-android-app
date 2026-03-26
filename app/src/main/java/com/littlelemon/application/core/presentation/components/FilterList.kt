package com.littlelemon.application.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@Composable
fun FilterList(
    filters: List<String>,
    selected: String,
    onSelect: (filter: String) -> Unit,
    modifier: Modifier = Modifier
) {

}


@Composable
fun FilterListItem(
    filterName: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        if (selected)
            MaterialTheme.colors.primaryDark
        else
            MaterialTheme.colors.primary
    )

    val borderColor: Color by animateColorAsState(
        if (selected)
            MaterialTheme.colors.transparent
        else
            MaterialTheme.colors.outlineSecondary
    )

    val contentColor = animateColorAsState(
        if (selected)
            MaterialTheme.colors.contentOnColor
        else
            MaterialTheme.colors.contentSecondary
    )



    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(backgroundColor)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2

                drawLine(borderColor, Offset(0f, y), Offset(size.width, y), strokeWidth)
            }
            .selectable(selected = selected, role = Role.DropdownList, onClick = onSelect)
            .padding(
                vertical = MaterialTheme.dimens.sizeLG,
                horizontal = MaterialTheme.dimens.sizeXL
            ), horizontalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.sizeLG
        ), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            filterName,
            style = MaterialTheme.typeStyle.bodyMedium,
            color = contentColor,
            modifier = Modifier.weight(1f)
        )
        AnimatedVisibility(selected, enter = scaleIn(), exit = scaleOut()) {
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.sizeLG))
            Image(
                painterResource(R.drawable.ic_checkcircle), null, colorFilter = ColorFilter.tint(
                    MaterialTheme.colors.contentAccent
                ), modifier = Modifier
                    .size(24.dp)
                    .testTag(CoreTestTags.FILTER_ITEM_CHECK)
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun FilterListItemPreview() {
    var selected by remember { mutableStateOf(false) }
    LittleLemonTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterListItem("Filter Item", true, {})
            FilterListItem("Filter Item", false, {})
            FilterListItem("Selectable Filter Item", selected, { selected = !selected })
        }
    }
}


@Preview
@Composable
private fun FilterListPreview() {
    LittleLemonTheme {
//        FilterList()
    }
}