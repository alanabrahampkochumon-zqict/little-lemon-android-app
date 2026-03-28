package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.components.HeaderTypeStyle
import com.littlelemon.application.core.presentation.components.OptionSelect
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.home.presentation.HomeViewModel
import com.littlelemon.application.reservation.domain.models.Reservation
import com.littlelemon.application.reservation.presentation.screens.components.ReservationCard
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

@Composable
fun HomeScreenContent(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    HomeScreenRoot(modifier)
}


@Composable
fun HomeScreenRoot(modifier: Modifier = Modifier) {
    val homeOptions = listOf("Food Delivery", "Reserve a Table")
    // TODO: Add navigation switch using navgraphs
    var currentSelection by remember { mutableStateOf(homeOptions[0]) }

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
    // TODO: Remove content padding and apply padding to children.
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(MaterialTheme.dimens.sizeXL),
    ) {
        item {

            Spacer(Modifier.height(MaterialTheme.dimens.size2XL))
            OptionSelect(
                homeOptions,
                selectedOption = currentSelection,
                onSelectionChange = { currentSelection = it },
            )
            Spacer(Modifier.height(MaterialTheme.dimens.size3XL))

            // Upcoming reservations | Conditionally render
            Header(
                label = pluralStringResource(
                    R.plurals.heading_upcoming_reservation,
                    reservations.size
                ),
                typeStyle = HeaderTypeStyle.Secondary,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeMD))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.sizeLG
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(reservations) { reservation ->
                    ReservationCard(
                        reservation = reservation,
                        {/* TODO */ },
                        modifier = Modifier.width(cardWidth)
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size3XL))
        }
        foodDeliveryContent()
    }
}


@Preview(showBackground = true, backgroundColor = 0xF5F5F6)
@Composable
private fun HomeScreenPreview() {
    LittleLemonTheme {
        HomeScreenRoot()
    }
}