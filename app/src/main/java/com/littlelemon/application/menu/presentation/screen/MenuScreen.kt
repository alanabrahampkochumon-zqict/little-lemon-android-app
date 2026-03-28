package com.littlelemon.application.menu.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.home.presentation.components.CategoryCard
import com.littlelemon.application.menu.presentation.MenuViewModel

@Composable
fun MenuScreen(viewModel: MenuViewModel, modifier: Modifier = Modifier) {
    MenuScreenRoot(modifier)
}


@Composable
fun MenuScreenRoot(modifier: Modifier = Modifier) {
    val categories =
        listOf("All", "Lunch", "Mains", "Dessert", "La Casa", "Specials", "Chef Specials")
    val currentCategory = categories[0]
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(MaterialTheme.dimens.sizeXL)) {
        item {
            Spacer(Modifier.height(MaterialTheme.dimens.size2XL))
            Header(
                label = stringResource(R.string.heading_explore_our_cuisines),
                typeStyle = HeaderTypeStyle.Primary,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.sizeMD
                    ),
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Image(
                        painterResource(R.drawable.ic_settings),
                        null,
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colors.contentHighlight
                        )
                    )
                    Text(
                        stringResource(R.string.act_filter),
                        style = MaterialTheme.typeStyle.labelMedium,
                        color = MaterialTheme.colors.contentHighlight
                    )
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeMD))
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.sizeMD
                ),
                modifier = Modifier,
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category, selected = category == currentCategory, {/* TODO */ })
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xf6f5f5)
@Composable
private fun MenuScreenRootPreview() {

    LittleLemonTheme {
        MenuScreenRoot()
    }
}