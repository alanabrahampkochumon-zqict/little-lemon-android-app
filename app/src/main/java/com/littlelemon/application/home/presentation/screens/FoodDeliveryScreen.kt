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
import androidx.compose.foundation.lazy.LazyColumn
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
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.menu.presentation.screen.components.MenuCard
import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt
import kotlin.random.Random

// TODO: Remove
fun generateDish(): Dish {
    val nutritionInfo = NutritionInfo(
        calories = (Math.random() * 1000).roundToInt(),
        protein = (Math.random() * 1000).roundToInt(),
        carbs = (Math.random() * 1000).roundToInt(),
        fats = (Math.random() * 1000).roundToInt(),
    )
    return Dish(
        title = "Greek Salad",
        description = "The famous greek salad of crispy lettuce, peppers, olives and our Chicago style feta cheese, garnished with crunchy garlic and rosemary croutons",
        price = Math.random() * 1000,
        imageURL = "",
        stock = (Math.random() * 1000).roundToInt(),
        nutritionInfo = nutritionInfo,
        discountedPrice = Math.random() * 1000,
        popularityIndex = (0..100).random(),
        dateAdded = LocalDateTime(2024, 5, 5, 10, 12, 0),
        category = listOf()
    )
}

@Composable
fun FoodDeliveryScreen(modifier: Modifier = Modifier) {
    // TODO: Replace with state
    val categories = listOf("Lunch", "Mains", "Dessert", "La Casa", "Specials", "Chef Specials")

    val dishes = List(10) { generateDish() }
    // TODO: EndReplace

    Column (
        modifier = modifier
            .fillMaxSize()
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
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeXS))

        LazyColumn(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.sizeXL),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.dimens.size2XL
            )
        ) {
            items(dishes) { dish ->
                MenuCard(dish, Random.nextInt(5), {}, {})
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