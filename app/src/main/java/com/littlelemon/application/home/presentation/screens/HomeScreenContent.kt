package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.home.presentation.HomeState
import com.littlelemon.application.home.presentation.HomeViewModel
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.reservation.domain.models.Reservation
import com.littlelemon.application.reservation.presentation.screens.components.ReservationCard
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.days

@Composable
fun HomeScreenContent(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    val homeState by viewModel.state.collectAsStateWithLifecycle()
    HomeScreenContentRoot(homeState, {/* TODO */ }, {/* TODO */ }, {/* TODO */ }, modifier)
}


@Composable
fun HomeScreenContentRoot(
    state: HomeState,
    onCategoryChange: (String) -> Unit,
    onViewAll: () -> Unit,
    onGetRoute: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardWidth = 340.dp
    // TODO: Replace with state
    val currentInstant = LocalDateTime(2025, Month.MAY, 12, 14, 30).toInstant(TimeZone.UTC)
    val futureInstant = currentInstant + 5.days
    val pastInstant = currentInstant - 5.days
    val reservation1 = Reservation(
        futureInstant.toLocalDateTime(TimeZone.currentSystemDefault()),
        reservedDate = pastInstant.toLocalDateTime(TimeZone.currentSystemDefault()),
        reservationStatus = Reservation.ReservationStatus.Upcoming,
        reservedFor = 5,
    )
    val reservation2 = Reservation(
        futureInstant.toLocalDateTime(TimeZone.currentSystemDefault()),
        reservedDate = pastInstant.toLocalDateTime(TimeZone.currentSystemDefault()),
        reservationStatus = Reservation.ReservationStatus.Cancelled,
        reservedFor = 5,
    )
    val reservation3 = Reservation(
        pastInstant.toLocalDateTime(TimeZone.currentSystemDefault()),
        reservedDate = (pastInstant - 30.days).toLocalDateTime(TimeZone.currentSystemDefault()),
        reservationStatus = Reservation.ReservationStatus.Expired,
        reservedFor = 5,
    )
    val reservations = listOf(reservation1, reservation2, reservation3)

    val contentPadding = LittleLemonTheme.dimens.sizeXL

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {

            Spacer(Modifier.height(LittleLemonTheme.dimens.size2XL))

            // Upcoming reservations | Conditionally render
            Header(
                label = pluralStringResource(
                    R.plurals.heading_upcoming_reservation, reservations.size
                ),
                typeStyle = HeaderTypeStyle.Secondary,
                modifier = Modifier.padding(horizontal = contentPadding)
            )
            Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.sizeMD))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(
                    LittleLemonTheme.dimens.sizeLG
                ),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = contentPadding)
            ) {
                items(reservations) { reservation ->
                    ReservationCard(
                        reservation = reservation,
                        onGetRoute = onGetRoute,
                        modifier = Modifier.width(cardWidth)
                    )
                }
            }

            Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.size3XL))
        }
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
            ), {}, {}, {}
        )
    }
}