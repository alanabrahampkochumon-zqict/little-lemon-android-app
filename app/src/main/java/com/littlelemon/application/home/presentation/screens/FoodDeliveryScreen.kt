package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.home.presentation.components.CategoryCard

@Composable
fun FoodDeliveryScreen(modifier: Modifier = Modifier) {
    // TODO: Replace with state
    val categories = listOf("Lunch", "Mains", "Dessert", "La Casa", "Specials", "Chef Specials")

    // TODO: EndReplace


    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
        ) {


            // Order for delivery
            Header(
                label = stringResource(R.string.heading_order_for_delivery),
                typeStyle = HeaderTypeStyle.Secondary,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.sizeXL)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeMD))
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.sizeMD
                ),
                modifier = Modifier,
                contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.sizeXL)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category,
                        selected = false,
                        {/* TODO */ })
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2XL))
            Header(
                label = stringResource(R.string.heading_popular_orders),
                typeStyle = HeaderTypeStyle.Primary,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.sizeXL)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.view_all),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clickable(
                            role = Role.Button, indication = null, onClick = { /* TODO */ },
                            interactionSource = remember { MutableInteractionSource() },
                            enabled = true,
                            onClickLabel = stringResource(R.string.view_all)
                        ),
                    style = MaterialTheme.typeStyle.labelMedium,
                    color = MaterialTheme.colors.contentHighlight,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xF5F5F6)
@Composable
private fun FoodDeliveryScreenPreview() {
    LittleLemonTheme {
        FoodDeliveryScreen()
    }
}