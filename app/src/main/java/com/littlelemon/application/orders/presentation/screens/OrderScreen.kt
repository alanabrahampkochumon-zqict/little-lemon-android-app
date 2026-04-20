package com.littlelemon.application.orders.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

// TODO: Add VM
@Composable
fun OrderScreen(modifier: Modifier = Modifier) {
    OrderScreenRoot(modifier)
}

@Composable
fun OrderScreenRoot(modifier: Modifier = Modifier) {
    val contentPadding = LittleLemonTheme.dimens.sizeXL
    LazyColumn(modifier = modifier) {
        item {
            Spacer(Modifier.height(LittleLemonTheme.dimens.size2XL))
            Header(
                stringResource(R.string.heading_your_orders),
                typeStyle = HeaderTypeStyle.Primary,
                modifier = Modifier.padding(horizontal = contentPadding)
            ) {
                // TODO: Add click listener
                Row(
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(
                        stringResource(R.string.act_most_recent),
                        style = LittleLemonTheme.typography.labelMedium,
                        color = LittleLemonTheme.colors.contentHighlight
                    )
                    Spacer(Modifier.width(LittleLemonTheme.dimens.sizeMD))
                    Image(
                        painterResource(R.drawable.ic_chevron_down),
                        null,
                        colorFilter = ColorFilter.tint(LittleLemonTheme.colors.contentHighlight)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xf5f6f6)
@Composable
private fun OrderScreenRootPreview() {
    LittleLemonTheme {
        OrderScreenRoot()
    }
}