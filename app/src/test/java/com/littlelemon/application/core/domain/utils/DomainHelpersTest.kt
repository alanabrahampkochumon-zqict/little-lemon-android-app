package com.littlelemon.application.core.domain.utils

import kotlinx.datetime.TimeZone
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DomainHelpersTest {


    @Nested
    inner class DateTimeConverterTests {

        @Test
        fun givenStandardTimestamp_whenConvertedToDatetimeUTC_returnsCorrectTime() {
            // Arrange
            val millis = 1672531200000L

            // Act
            val dateTime = millis.toLocalDateTime(TimeZone.UTC)

            // Assert
            assertEquals("2023-01-01T00:00", dateTime.toString())
        }

        @Test
        fun givenZero_whenConvertedToDatetimeUTC_returnsZeroPoint1970() {
            // Arrange
            val millis = 0L

            // Act
            val dateTime = millis.toLocalDateTime(TimeZone.UTC)

            // Assert
            assertEquals("1970-01-01T00:00", dateTime.toString())
        }

        @Test
        fun givenLeapYearMillis_whenConvertedToDatetimeUTC_returnsCorrectTime() {
            // Arrange
            val millis = 1709164800000L

            // Act
            val dateTime = millis.toLocalDateTime(TimeZone.UTC)

            // Assert
            assertEquals("2024-02-29T00:00", dateTime.toString())
        }

        @Test
        fun givenStandardTimestamp_whenConvertedToDatetimeIST_returnsCorrectTime() {
            // Arrange
            val millis = 1672531200000L
            // Act
            val dateTime = millis.toLocalDateTime(TimeZone.of("Asia/Kolkata"))

            // Assert
            assertEquals("2023-01-01T05:30", dateTime.toString())
        }
    }

}