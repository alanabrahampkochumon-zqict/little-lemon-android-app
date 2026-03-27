package com.littlelemon.application.reservation.presentation.screens.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import com.littlelemon.application.reservation.domain.models.Reservation
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue
import kotlin.time.Clock

@RunWith(RobolectricTestRunner::class)
class ReservationCardTest {

    @get:Rule
    val testRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()
    private val timeZone = TimeZone.UTC

    private val day = 5
    private val hour = 15
    private val minute = 30
    private val expectedTime = "03:30 PM"
    private val expectedMonth = "Dec"
    private val expectedDay = "05"
    val reservationDateTime = LocalDateTime(2024, Month.DECEMBER, day, hour, minute, 0)
    val reservedDateTime =
        Clock.System.now().minus(3, DateTimeUnit.DAY, timeZone).toLocalDateTime(timeZone)

    val tableCount = 5
    private val reservation = Reservation(
        reservationDateTime,
        reservedDateTime,
        Reservation.ReservationStatus.Upcoming,
        tableCount
    )

    private val expectedTitle = application.getString(R.string.reservation_card_title, tableCount)
    private val expectedReservedDate =
        application.getString(R.string.reserved_time_ago, "3 days ago")

    @Test
    fun displaysCorrectTitle() {
        // When a reservation card is displayed
        testRule.setContent {
            ReservationCard(reservation, {})
        }

        // Then it displays correct month
        testRule.onNodeWithText(expectedTitle).assertIsDisplayed()
    }

    @Test
    fun displaysCorrectMonth() {
        // When a reservation card is displayed
        testRule.setContent {
            ReservationCard(reservation, {})
        }

        // Then it displays correct month
        testRule.onNodeWithText(expectedMonth, ignoreCase = true).assertIsDisplayed()
    }

    @Test
    fun displaysCorrectDay() {
        // When a reservation card is displayed
        testRule.setContent {
            ReservationCard(reservation, {})
        }

        // Then it displays correct day
        testRule.onNodeWithText(day.toString()).assertIsDisplayed()
    }

    @Test
    fun displaysCorrectTime() {
        // When a reservation card is displayed
        testRule.setContent {
            ReservationCard(reservation, {})
        }

        // Then it displays correct day
        testRule.onNodeWithText(expectedTime).assertIsDisplayed()
    }

    @Test
    fun displaysCorrectReservedTime() {
        // When a reservation card is displayed
        testRule.setContent {
            ReservationCard(reservation, {})
        }

        // Then it displays correct reserved date
        testRule.onNodeWithText(expectedReservedDate).assertIsDisplayed()
    }

    @Test
    fun upcomingReservation_getRouteDisplayed() {
        // When a reservation card is displayed with upcoming reservation
        testRule.setContent {
            ReservationCard(
                reservation.copy(reservationStatus = Reservation.ReservationStatus.Upcoming),
                {})
        }

        // Then it displays get route button
        testRule.onNodeWithText(application.getString(R.string.act_get_route)).assertIsDisplayed()
    }

    @Test
    fun cancelledReservation_getRouteIsNotDisplayed() {
        // When a reservation card is displayed with canceled reservation
        testRule.setContent {
            ReservationCard(
                reservation.copy(reservationStatus = Reservation.ReservationStatus.Cancelled),
                {})
        }

        // Then it does not display get route button
        testRule.onNodeWithText(application.getString(R.string.act_get_route))
            .assertIsNotDisplayed()
    }

    @Test
    fun expiredReservation_getRouteIsNotDisplayed() {
        // When a reservation card is displayed with expired reservation
        testRule.setContent {
            ReservationCard(
                reservation.copy(reservationStatus = Reservation.ReservationStatus.Expired),
                {})
        }

        // Then it does not display get route button
        testRule.onNodeWithText(application.getString(R.string.act_get_route))
            .assertIsNotDisplayed()
    }

    @Test
    fun onGetRouteClick_triggersCallback() {
        // When a reservation card is displayed with upcoming reservation
        var callbackTriggered = false
        testRule.setContent {
            ReservationCard(
                reservation.copy(reservationStatus = Reservation.ReservationStatus.Upcoming),
                { callbackTriggered = true })
        }

        // When get route is pressed
        testRule.onNodeWithText(application.getString(R.string.act_get_route)).performClick()

        // Then it triggers callback
        assertTrue(callbackTriggered)
    }

}