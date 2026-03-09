package com.littlelemon.application.menu.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


@Composable
fun MenuCard(
    dish: Dish,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 240.dp)
                .fillMaxHeight()
                .background(Color.Green)
        ) {

        }
        Column(modifier = Modifier.background(MaterialTheme.colors.primary)) {

            Text(dish.title)
            dish.description?.let {
                Text(it)
            }
            Text(dish.price.toString())
            Text(dish.discountedPrice.toString())
        }
    }
}


@Preview
@Composable
private fun MenuCardPreview() {
    LittleLemonTheme {
        MenuCard(
            Dish(
                title = "Grilled Whole Fish",
                description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with  extra-virgin olive oil (EVOO), the primary source of monounsaturated  fats in this diet",
                price = 29.85,
                imageURL = "NO URL",
                stock = 15,
                nutritionInfo = NutritionInfo(155, 22, 15, 9),
                discountedPrice = 15.83,
                category = listOf(),
                dateAdded = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                popularityIndex = 392
            )
        )
    }
}