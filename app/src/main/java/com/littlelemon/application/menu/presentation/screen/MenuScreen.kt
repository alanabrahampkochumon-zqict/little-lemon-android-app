package com.littlelemon.application.menu.presentation.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.home.presentation.components.CategoryCard
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.menu.presentation.MenuState
import com.littlelemon.application.menu.presentation.MenuViewModel
import com.littlelemon.application.menu.presentation.screen.components.MenuCard
import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun MenuScreen(viewModel: MenuViewModel, modifier: Modifier = Modifier) {
    val menuState by viewModel.state.collectAsStateWithLifecycle()
    Log.d("State", menuState.dishes.toString())
    Log.d("State", menuState.toString())
    MenuScreenRoot(menuState, modifier)
}

// TODO: Add test
@Composable
fun MenuScreenRoot(menuState: MenuState, modifier: Modifier = Modifier) {
    val contentPadding = MaterialTheme.dimens.sizeXL

    if (menuState.dishes == null) {
        // TODO: ERROR UI
        Text("There was an error loading the dishes.")
        return;
    }

    val categories = menuState.dishes.fold(
        mutableSetOf<Category>()
    ) { categories, dish ->
        categories.addAll(dish.category)
        categories
    }.map { it.categoryName }
    Log.d("Cats", categories.toString())
    val currentCategory by remember {
        mutableStateOf("")
    }

    LazyColumn(modifier = modifier) {
        item {
            Spacer(Modifier.height(MaterialTheme.dimens.size2XL))
            Header(
                label = stringResource(R.string.heading_explore_our_cuisines),
                typeStyle = HeaderTypeStyle.Primary,
                modifier = Modifier.padding(horizontal = contentPadding)
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
                contentPadding = PaddingValues(horizontal = contentPadding),
                modifier = Modifier,
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category, selected = category == currentCategory, {/* TODO */ })
                }
            }
        }
        item { Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2XL)) }
        items(menuState.dishes) { dish ->
            MenuCard(
                dish,
                Random.nextInt(5),
                {},
                {},
                modifier = Modifier.padding(horizontal = contentPadding)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2XL))
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xf6f5f5)
@Composable
private fun MenuScreenRootPreview() {
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

    val dishes = List(10) { generateDish() }
    val state = MenuState(dishes)
    LittleLemonTheme {
        MenuScreenRoot(state)
    }
}