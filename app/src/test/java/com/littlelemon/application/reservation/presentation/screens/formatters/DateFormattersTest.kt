package com.littlelemon.application.reservation.presentation.screens.formatters

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.time.Clock

class DateFormattersTest {

    companion object {

        private val timezone = TimeZone.UTC
        private val currentTime = Clock.System.now()
        private val currentMonth =
            currentTime.toLocalDateTime(timezone).month.toString().substring(0, 3)
        private val currentDay =
            currentTime.toLocalDateTime(timezone).day
        private val currentYear =
            currentTime.toLocalDateTime(timezone).year

        @JvmStatic
        fun dateTimeProvider(): Stream<Arguments> = Stream.of(
            arguments(currentTime.minus(5, DateTimeUnit.SECOND).toLocalDateTime(timezone), "now"),
            arguments(
                currentTime.minus(1, DateTimeUnit.MINUTE).toLocalDateTime(timezone),
                "1 minutes ago"
            ),
            arguments(
                currentTime.minus(5, DateTimeUnit.MINUTE).toLocalDateTime(timezone),
                "5 minutes ago"
            ),
            arguments(
                currentTime.minus(1, DateTimeUnit.HOUR).toLocalDateTime(timezone),
                "1 hours ago"
            ),
            arguments(
                currentTime.minus(5, DateTimeUnit.HOUR).toLocalDateTime(timezone),
                "5 hours ago"
            ),
            arguments(
                currentTime.minus(1, DateTimeUnit.DAY, timezone).toLocalDateTime(timezone),
                "1 days ago"
            ),
            arguments(
                currentTime.minus(5, DateTimeUnit.DAY, timezone).toLocalDateTime(timezone),
                "5 days ago"
            ),
            arguments(
                currentTime.minus(8, DateTimeUnit.DAY, timezone).toLocalDateTime(timezone),
                "1 weeks ago"
            ),
            arguments(
                currentTime.minus(3, DateTimeUnit.WEEK, timezone).toLocalDateTime(timezone),
                "3 weeks ago"
            ),
            arguments(
                currentTime.minus(5, DateTimeUnit.WEEK, timezone).toLocalDateTime(timezone),
                "1 months ago"
            ),
            arguments(
                currentTime.minus(6, DateTimeUnit.MONTH, timezone).toLocalDateTime(timezone),
                "6 months ago"
            ),
            arguments(
                currentTime.minus(2, DateTimeUnit.YEAR, timezone).toLocalDateTime(timezone),
                "on $currentMonth/$currentDay/${currentYear - 2}"
            ),
        )
    }

    @Nested
    inner class TwelveHourClockFormatTest {
        @Test
        fun correctlyFormatAMTime() {
            // Given an AM datetime
            val dateTime =
                LocalDateTime(year = 2026, month = 12, day = 2, hour = 9, minute = 30, second = 35)
            val expectedDateTime = "09:30 AM"
            // When formatted
            val formatted = dateTime.formatTimeAsTwelveHourClock()
            // Then, it displays the correct time
            assertEquals(expectedDateTime, formatted)
        }

        @Test
        fun correctlyFormatsPMTime() {
            // Given a PM datetime
            val dateTime =
                LocalDateTime(year = 2026, month = 12, day = 2, hour = 23, minute = 30, second = 35)
            val expectedDateTime = "11:30 PM"
            // When formatted
            val formatted = dateTime.formatTimeAsTwelveHourClock()
            // Then, it displays the correct time
            assertEquals(expectedDateTime, formatted)
        }

        @Test
        fun correctlyFormatsMidnightTime() {
            // Given midnight hour
            val dateTime =
                LocalDateTime(year = 2026, month = 12, day = 2, hour = 0, minute = 0, second = 35)
            val expectedDateTime = "12:00 AM"

            // When formatted
            val formatted = dateTime.formatTimeAsTwelveHourClock()
            // Then, it displays the correct time
            assertEquals(expectedDateTime, formatted)
        }
    }

    @ParameterizedTest
    @MethodSource("dateTimeProvider")
    fun timeDistanceFormatter_returnsCorrectFormattedTime(
        dateTime: LocalDateTime,
        expected: String
    ) {
        assertEquals(expected, dateTime.toTimeDistance(timezone))
    }

}