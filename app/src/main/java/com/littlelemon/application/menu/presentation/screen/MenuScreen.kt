package com.littlelemon.application.menu.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.home.presentation.components.CategoryCard
import com.littlelemon.application.menu.MenuTestTags
import com.littlelemon.application.menu.presentation.CategoryState
import com.littlelemon.application.menu.presentation.DishUiState
import com.littlelemon.application.menu.presentation.MenuActions
import com.littlelemon.application.menu.presentation.MenuState
import com.littlelemon.application.menu.presentation.MenuViewModel
import com.littlelemon.application.menu.presentation.screen.components.MenuCard
import com.littlelemon.application.shared.menu.domain.models.Category
import com.littlelemon.application.shared.menu.domain.models.Dish
import com.littlelemon.application.shared.menu.domain.models.NutritionInfo
import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun MenuScreen(viewModel: MenuViewModel, modifier: Modifier = Modifier) {
    val menuState by viewModel.state.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val currentCategory by viewModel.currentCategory.collectAsStateWithLifecycle()
    MenuScreenRoot(
        menuState,
        categories,
        currentCategory,
        { viewModel.onAction(MenuActions.UpdateDishCategory(it)) },
        { /** TODO */ },
        { /** TODO */ },
        modifier
    )
}

@Composable
fun MenuScreenRoot(
    menuState: MenuState,
    categories: CategoryState,
    currentCategory: String?,
    onCategoryChanged: (String?) -> Unit,
    onIncreaseQuantity: (Dish) -> Unit,
    onDecreaseQuantity: (Dish) -> Unit,
    modifier: Modifier = Modifier
) {

    val placeholder = painterResource(R.drawable.illustration_image_loading)
    val contentPadding = LittleLemonTheme.dimens.sizeXL

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        verticalArrangement = Arrangement.spacedBy(
            LittleLemonTheme.dimens.size2XL
        ),
        horizontalArrangement = Arrangement.spacedBy(
            LittleLemonTheme.dimens.size2XL
        ),
        modifier = modifier
            .fillMaxSize()
            .testTag(MenuTestTags.MENU_ITEM_LIST),
        contentPadding = PaddingValues(
            vertical = contentPadding,
            horizontal = contentPadding
        )
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Header(
                    label = stringResource(R.string.heading_explore_our_cuisines),
                    typeStyle = HeaderTypeStyle.Primary,
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(
                            LittleLemonTheme.dimens.sizeMD
                        ),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Image(
                            painterResource(R.drawable.ic_settings),
                            null,
                            colorFilter = ColorFilter.tint(
                                LittleLemonTheme.colors.contentHighlight
                            )
                        )
                        Text(
                            stringResource(R.string.act_filter),
                            style = LittleLemonTheme.typography.labelMedium,
                            color = LittleLemonTheme.colors.contentHighlight
                        )
                    }
                }
                Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.sizeXS))
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        LittleLemonTheme.dimens.sizeMD
                    ),
                    contentPadding = PaddingValues(horizontal = contentPadding),
                    modifier = Modifier
                        .layout { measurable, constraint ->
                            // To prevent the lazy row from cutting off due to content padding of the LazyVerticalGrid(parent)
                            // we need to force the LazyRow to be larger than it's calculated width
                            val padding = contentPadding.roundToPx()

                            // Manually add the extra padding to already calculated max width
                            val width = constraint.maxWidth + (padding * 2)

                            val expandedConstraints =
                                constraint.copy(maxWidth = width, minWidth = width)

                            // Measure the view with new constraints and place it at the origin point
                            // Since we applied content padding, it will be placed at the correct position
                            val placeable = measurable.measure(expandedConstraints)
                            layout(placeable.width, placeable.height) {
                                placeable.place(0, 0)
                            }

                        },
                ) {
                    item {
                        CategoryCard(
                            stringResource(R.string.all_category),
                            selected = currentCategory == null,
                            { onCategoryChanged(null) })
                    }
                    this@LazyRow.items(categories.categories) { category -> // TODO: Replace
                        CategoryCard(
                            category.categoryName,
                            selected = category.categoryName == currentCategory,
                            { onCategoryChanged(category.categoryName) })
                    }

                }
            }
        }

        if (menuState.dishes == null) {
            // TODO: ERROR UI
            item { Text("There was an error loading the dishes.") }
        } else {
            items(menuState.dishes, key = { it.dish.id }) { dishState ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                )
                MenuCard(
                    dishState.dish,
                    Random.nextInt(5),
                    { onIncreaseQuantity(dishState.dish) },
                    { onDecreaseQuantity(dishState.dish) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = placeholder
                )
            }
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
            category = listOf(
                Category("Category 1"),
                Category("Category 2"),
                Category("Category 3"),
                Category("Category 4"),
            )
        )
    }

    val dishes = List(10) { generateDish() }
    val categories = dishes.first().category
    val state = MenuState(dishes.map { DishUiState(it, Random.nextInt(0, 3)) })
    LittleLemonTheme {
        MenuScreenRoot(state, CategoryState(categories = categories), null, {}, {}, {})
    }
}