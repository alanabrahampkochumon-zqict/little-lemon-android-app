package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

enum class HeaderTypeStyle {
    Primary,
    Secondary
}


@Composable
fun Header(
    label: String,
    modifier: Modifier = Modifier,
    typeStyle: HeaderTypeStyle = HeaderTypeStyle.Primary,
    actionContent: @Composable () -> Unit = {}
) {
    val style = if (typeStyle == HeaderTypeStyle.Primary) {
        LittleLemonTheme.typography.displayLarge
    } else {
        LittleLemonTheme.typography.headlineSmall
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = style,
            color = LittleLemonTheme.colors.contentPrimary,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(LittleLemonTheme.dimens.sizeLG))
        actionContent()
    }
}


@Preview(showBackground = true)
@Composable
private fun HeaderPreview() {
    LittleLemonTheme {
        Column {
            Header("Header") {
                Text("action")
            }
            Spacer(Modifier.height(16.dp))
            Header("Header", typeStyle = HeaderTypeStyle.Secondary) {
                Text("action")
            }
        }
    }
}