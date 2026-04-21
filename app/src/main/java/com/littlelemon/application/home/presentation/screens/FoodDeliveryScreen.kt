package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.home.presentation.HomeState
import com.littlelemon.application.home.presentation.components.CategoryCard
import com.littlelemon.application.menu.presentation.screen.components.MenuCard
import kotlin.random.Random

fun LazyListScope.foodDeliveryContent(
    state: HomeState, onCategoryChange: (String) -> Unit, onViewAll: () -> Unit, contentPadding: Dp
) {
    item {
        // Order for delivery
        Header(
            label = stringResource(R.string.heading_order_for_delivery),
            typeStyle = HeaderTypeStyle.Secondary,
            modifier = Modifier.padding(horizontal = contentPadding)
        )
        Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.sizeMD))
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                LittleLemonTheme.dimens.sizeMD
            ),
            contentPadding = PaddingValues(horizontal = contentPadding)
        ) {
            items(state.categories) { (categoryName) ->
                CategoryCard(
                    categoryName, selected = false, { onCategoryChange(categoryName) })
            }
        }
        Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.size2XL))

    }
    item {
        Header(
            label = stringResource(R.string.heading_popular_orders),
            typeStyle = HeaderTypeStyle.Primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = contentPadding)
        ) {
            Text(
                text = stringResource(R.string.view_all),
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clickable(
                        role = Role.Button,
                        indication = null,
                        onClick = onViewAll,
                        interactionSource = remember { MutableInteractionSource() },
                        enabled = true,
                        onClickLabel = stringResource(R.string.view_all)
                    ),
                style = LittleLemonTheme.typography.labelMedium,
                color = LittleLemonTheme.colors.contentHighlight,
            )
        }
        Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.sizeXS))
    }

    items(state.popularDishes, key = { it.title + it.dateAdded.toString() }) { dish ->
        MenuCard(
            dish,
            Random.nextInt(5),
            {},
            {},
            modifier = Modifier.padding(horizontal = contentPadding)
        )
        Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.size2XL))
    }

}


//@Preview(showBackground = true, backgroundColor = 0xF5F5F6)
//@Composable
//private fun FoodDeliveryScreenPreview() {
//    LittleLemonTheme {
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            foodDeliveryContent(16.dp)
//        }
//    }
//}