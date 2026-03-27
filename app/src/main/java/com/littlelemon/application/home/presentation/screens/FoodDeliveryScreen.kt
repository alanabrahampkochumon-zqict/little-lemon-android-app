package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.home.presentation.components.CategoryCard
import com.littlelemon.application.reservation.domain.models.Reservation
import com.littlelemon.application.reservation.presentation.screens.components.ReservationCard
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

@Composable
fun FoodDeliveryScreen(modifier: Modifier = Modifier) {
    // TODO: Replace with state
    val currentInstant = LocalDateTime(2025, Month.MAY, 12, 14, 30).toInstant(TimeZone.UTC)
    val futureInstant = currentInstant + 5.days
    val pastInstant = currentInstant - 5.days
    val reservation = Reservation(
        futureInstant.toLocalDateTime(TimeZone.currentSystemDefault()),
        reservedDate = pastInstant.toLocalDateTime(TimeZone.currentSystemDefault()),
        reservationStatus = Reservation.ReservationStatus.Upcoming,
        reservedFor = 5,
    )

    val categories = listOf("Lunch", "Mains", "Dessert", "La Casa", "Specials", "Chef Specials")

    // TODO: EndReplace

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = MaterialTheme.dimens.sizeXL)
    ) {
        Column(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.sizeXL)) {
            Header(
                label = stringResource(R.string.heading_upcoming_reservation),
                typeStyle = HeaderTypeStyle.Secondary
            )
//            Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeMD))
            ReservationCard(reservation = reservation, {/* TODO */ })
        }
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size3XL))
        Header(
            label = stringResource(R.string.heading_order_for_delivery),
            typeStyle = HeaderTypeStyle.Secondary,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.sizeXL)
        )
        LazyRow(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.sizeMD), modifier = Modifier,
            contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.sizeXL)) {
            items(categories) { category ->
                CategoryCard(
                    category,
                    selected = false,
                    {/* TODO */ })
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