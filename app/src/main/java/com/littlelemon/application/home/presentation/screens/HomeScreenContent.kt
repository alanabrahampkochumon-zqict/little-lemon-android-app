package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.home.presentation.HomeState
import com.littlelemon.application.home.presentation.HomeViewModel
import com.littlelemon.application.shared.menu.domain.models.Category
import com.littlelemon.application.shared.menu.domain.models.Dish
import com.littlelemon.application.shared.menu.domain.models.NutritionInfo
import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt

@Composable
fun HomeScreenContent(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    val homeState by viewModel.state.collectAsStateWithLifecycle()
    HomeScreenContentRoot(homeState, {/* TODO */ }, {/* TODO */ }, modifier)
}


@Composable
fun HomeScreenContentRoot(
    state: HomeState,
    onCategoryChange: (String) -> Unit,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentPadding = LittleLemonTheme.dimens.size2XL

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = contentPadding)
    ) {
        foodDeliveryContent(state, onCategoryChange, onViewAll, contentPadding)
    }
}


@Preview(showBackground = true, backgroundColor = 0xF5F5F6)
@Composable
private fun HomeScreenContentPreview() {
    fun generateDish(): Dish {
        val nutritionInfo = NutritionInfo(
            calories = (Math.random() * 1000).roundToInt(),
            protein = (Math.random() * 1000).roundToInt(),
            carbs = (Math.random() * 1000).roundToInt(),
            fats = (Math.random() * 1000).roundToInt(),
        )
        return Dish(
            id = "",
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

    LittleLemonTheme {
        HomeScreenContentRoot(
            HomeState(
                popularDishes = List(5) { generateDish() }, categories = listOf(
                    Category("Category 1"),
                    Category("Category 2"),
                    Category("Category 3"),
                    Category("Category 4"),
                )
            ), {}, {}
        )
    }
}