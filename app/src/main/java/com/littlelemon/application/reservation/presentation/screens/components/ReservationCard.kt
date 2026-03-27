package com.littlelemon.application.reservation.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.littlelemon.application.core.presentation.components.Button
import com.littlelemon.application.core.presentation.components.ButtonVariant
import com.littlelemon.application.core.presentation.components.Tag
import com.littlelemon.application.core.presentation.components.TagVariant
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.utils.toComposeShadow
import com.littlelemon.application.core.presentation.utils.toTitleCase
import com.littlelemon.application.reservation.domain.models.Reservation
import com.littlelemon.application.reservation.presentation.screens.formatters.formatTimeAsTwelveHourClock
import com.littlelemon.application.reservation.presentation.screens.formatters.toTimeDistance
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
    val borderColor =
        if (upcomingReservation) MaterialTheme.colors.transparent else MaterialTheme.colors.outlineDisabled

    val tagVariant = if (upcomingReservation) TagVariant.SuccessLight else TagVariant.DisabledLight

    val innerCardShape = MaterialTheme.shapes.small
    val cardShape = MaterialTheme.shapes.medium

    val monthName = reservation.reservationDate.date.month.name.substring(0, 3).toTitleCase()
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
            .border(1.dp, borderColor, cardShape)
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
                reservation.reservationDate.date.day.toString(),
                color = contentColor,
                style = MaterialTheme.typeStyle.displayLarge,
                modifier = Modifier.offset(y = (-4).dp)
            )
            Text(
                reservation.reservationDate.formatTimeAsTwelveHourClock(),
                color = contentColor,
                style = MaterialTheme.typeStyle.bodyXSmall,
                modifier = Modifier.offset(y = (-6).dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    top = MaterialTheme.dimens.sizeMD,
                    start = MaterialTheme.dimens.sizeLG,
                    end = MaterialTheme.dimens.sizeMD
                )
        ) {
            Row {
                Text(
                    stringResource(R.string.reservation_card_title, reservation.reservedFor),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typeStyle.labelLarge,
                    color = MaterialTheme.colors.contentSecondary
                )
                Spacer(Modifier.width(MaterialTheme.dimens.sizeLG))
                Tag(reservation.reservationStatus.toString(), variant = tagVariant)
            }
            Spacer(Modifier.height(MaterialTheme.dimens.sizeXS))
            Text(
                stringResource(
                    R.string.reserved_time_ago,
                    reservation.reservedDate.toTimeDistance()
                ),
                style = MaterialTheme.typeStyle.bodyXSmall,
                color = MaterialTheme.colors.contentDisabled
            )
            if(upcomingReservation)
                Button(stringResource(R.string.act_get_route), variant = ButtonVariant.GHOST_HIGHLIGHT, onClick = onGetRoute)

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