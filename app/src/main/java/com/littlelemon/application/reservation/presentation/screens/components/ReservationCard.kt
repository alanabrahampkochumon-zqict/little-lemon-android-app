package com.littlelemon.application.reservation.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.utils.toComposeShadow
import com.littlelemon.application.reservation.domain.models.Reservation
import com.littlelemon.application.reservation.presentation.screens.formatters.formatTimeAsTwelveHourClock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

@Composable
fun ReservationCard(
    reservation: Reservation,
    onGetRoute: () -> Unit,
    modifier: Modifier = Modifier
) {
    val upcomingReservation =
        reservation.reservationStatus == Reservation.ReservationStatus.Upcoming
    val dateTimeSize = 100.dp

    val contentColor =
        if (upcomingReservation) MaterialTheme.colors.contentOnColor else MaterialTheme.colors.contentDisabled
    val backgroundColor =
        if (upcomingReservation) MaterialTheme.colors.success else MaterialTheme.colors.disabled

    val innerCardShape = MaterialTheme.shapes.small
    val cardShape = MaterialTheme.shapes.medium

    var monthName = reservation.reservedDate.date.month.name
    monthName = monthName[0].uppercase() + monthName.substring(1, 3).lowercase()
    val density = LocalDensity.current.density
    Row(
        modifier = modifier
            .then(
                if (upcomingReservation) Modifier.dropShadow(
                    cardShape,
                    MaterialTheme.shadows.dropSM.firstShadow.toComposeShadow(density)
                ) else Modifier
            )
            .background(MaterialTheme.colors.primary, cardShape)
            .clip(cardShape)
    ) {
        Column(
            modifier = Modifier
                .size(dateTimeSize)
                .background(backgroundColor, innerCardShape),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                monthName,
                color = contentColor,
                style = MaterialTheme.typeStyle.headlineSmall
            )
            Text(
                reservation.reservedDate.date.day.toString(),
                color = contentColor,
                style = MaterialTheme.typeStyle.displayLarge,
                modifier = Modifier.offset(y = (-4).dp)
            )
            Text(
                reservation.reservedDate.formatTimeAsTwelveHourClock(),
                color = contentColor,
                style = MaterialTheme.typeStyle.bodyXSmall,
                modifier = Modifier.offset(y = (-6).dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(stringResource(R.string.reservation_card_title, reservation.reservedFor))

        }
    }
}


@Preview(showBackground = true)
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