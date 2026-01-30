package com.littlelemon.application.menu.data.mappers.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ConvertCentsToDollarsTest {

    @Test
    fun zeroCents_whenConverted_returnsZeroDollars() {
        // Arrange

        // Act
        val dollars = convertCentsToDollars(0)

        // Assert
        assertEquals(0.0, dollars)
    }

    @Test
    fun givenCents_whenConverted_returnNonZeroDollar() {
        // Arrange
        val cents = listOf(1000L, 100L, 150L, 90L, -2000L)
        listOf(10.0, 1.0, 1.50, 0.90, -20.0)

        // Act
        val dollars = cents.map { cent -> convertCentsToDollars(cent) }

        // Assert
        assertTrue(dollars.mapIndexed { index, dollar -> dollars[index] == dollar }
            .all { matched -> matched })
    }
}