package com.littlelemon.application.reservation.presentation.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.reservation.domain.models.Reservation
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearMonth
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

@Composable
fun ReservationCard(
    reservation: Reservation,
    onGetRoute: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Column {
            Text(reservation.reservedDate.date.month.name)
//            Text(reservation.reservedDate.date.day)
//            Text(reservation.reservedDate.time.)
        }
    }
}


@Preview
@Composable
private fun ReservationCardPreview() {
    val currentInstant = Instant.fromEpochMilliseconds(System.currentTimeMillis())
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
    LittleLemonTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReservationCard(reservation1, {})
            ReservationCard(reservation2, {})
            ReservationCard(reservation3, {})
        }
    }
}