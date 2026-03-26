package com.littlelemon.application.reservation.presentation.screens.formatters

import kotlinx.datetime.LocalDateTime
import org.junit.Test
import kotlin.test.assertEquals

class DateFormattersTest {

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