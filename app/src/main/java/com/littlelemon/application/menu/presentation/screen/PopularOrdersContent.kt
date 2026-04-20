package com.littlelemon.application.menu.presentation.screen
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListScope
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Text
//import androidx.compose.material3.minimumInteractiveComponentSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.pluralStringResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.semantics.Role
//import androidx.compose.ui.unit.Dp
//import com.littlelemon.application.R
//import com.littlelemon.application.core.presentation.components.Header
//import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
//import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
//import com.littlelemon.application.home.presentation.components.CategoryCard
//import com.littlelemon.application.home.presentation.screens.generateDish
//import com.littlelemon.application.menu.domain.models.Category
//import com.littlelemon.application.menu.domain.models.Dish
//import com.littlelemon.application.menu.presentation.CategoryState
//import com.littlelemon.application.menu.presentation.MenuState
//import com.littlelemon.application.menu.presentation.MenuViewModel
//import com.littlelemon.application.menu.presentation.screen.components.MenuCard
//import com.littlelemon.application.reservation.presentation.screens.components.ReservationCard
//import kotlin.random.Random
//
//@Composable
//fun PopularOrdersContent(viewModel: MenuViewModel, modifier: Modifier = Modifier) {
//
//}
//
//// TODO: Add tests
//@Composable
//fun PopularOrdersRoot(
//    menuState: MenuState,
//    categoryState: CategoryState,
//    onCategorySelect: (category: Category) -> Unit,
//    onViewAll: () -> Unit,
//    onIncreaseQuantity: (dish: Dish) -> Unit,
//    onDecreaseQuantity: (dish: Dish) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        item {
//
//            Spacer(Modifier.height(LittleLemonTheme.dimens.size2XL))
//
//            // Upcoming reservations | Conditionally render
//            Header(
//                label = pluralStringResource(
//                    R.plurals.heading_upcoming_reservation,
//                    reservations.size
//                ),
//                typeStyle = HeaderTypeStyle.Secondary,
//                modifier = Modifier.padding(horizontal = contentPadding)
//            )
//            Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.sizeMD))
//            LazyRow(
//                horizontalArrangement = Arrangement.spacedBy(
//                    LittleLemonTheme.dimens.sizeLG
//                ),
//                modifier = Modifier.fillMaxWidth(),
//                contentPadding = PaddingValues(horizontal = contentPadding)
//            ) {
//                items(reservations) { reservation ->
//                    ReservationCard(
//                        reservation = reservation,
//                        {/* TODO */ },
//                        modifier = Modifier.width(cardWidth)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.size3XL))
//        }
//        foodDeliveryContent(contentPadding)
//    }
//}
//
//
//fun LazyListScope.foodDeliveryContent(contentPadding: Dp) {
//    // TODO: Replace with state
//    val categories = listOf("Lunch", "Mains", "Dessert", "La Casa", "Specials", "Chef Specials")
//
//    val dishes = List(10) { generateDish() }
//    // TODO: EndReplace
//
//    item {
//
//        // Order for delivery
//        Header(
//            label = stringResource(R.string.heading_order_for_delivery),
//            typeStyle = HeaderTypeStyle.Secondary,
//            modifier = Modifier.padding(horizontal = contentPadding)
//        )
//        Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.sizeMD))
//        LazyRow(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(
//                LittleLemonTheme.dimens.sizeMD
//            ),
//            contentPadding = PaddingValues(horizontal = contentPadding)
//        ) {
//            items(categories) { category ->
//                CategoryCard(
//                    category, selected = false, {/* TODO */ })
//            }
//        }
//        Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.size2XL))
//
//    }
//    item {
//        Header(
//            label = stringResource(R.string.heading_popular_orders),
//            typeStyle = HeaderTypeStyle.Primary,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = contentPadding)
//        ) {
//            Text(
//                text = stringResource(R.string.view_all),
//                modifier = Modifier
//                    .minimumInteractiveComponentSize()
//                    .clickable(
//                        role = Role.Button,
//                        indication = null,
//                        onClick = { /* TODO */ },
//                        interactionSource = remember { MutableInteractionSource() },
//                        enabled = true,
//                        onClickLabel = stringResource(R.string.view_all)
//                    ),
//                style = LittleLemonTheme.typography.labelMedium,
//                color = LittleLemonTheme.colors.contentHighlight,
//            )
//        }
//        Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.sizeXS))
//    }
//
//    // TODO: add id to dish and key
//    items(dishes) { dish ->
//        MenuCard(
//            dish,
//            Random.nextInt(5),
//            {},
//            {},
//            modifier = Modifier.padding(horizontal = contentPadding)
//        )
//        Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.size2XL))
//    }
//
//}
