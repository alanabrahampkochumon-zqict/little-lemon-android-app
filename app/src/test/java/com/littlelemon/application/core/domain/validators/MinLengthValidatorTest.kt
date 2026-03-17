package com.littlelemon.application.core.domain.validators

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class MinLengthValidatorTest {

    private val minLength = 10
    private val validator = MinLengthValidator(minLength)

    @Test
    fun minLengthValidator_emptyString_returnsFalse() {
        // When an empty string is validated
        val result = validator("")
        // Then, it returns false
        assertFalse(result)
    }

    @Test
    fun minLengthValidator_nMinusOneLengthString_returnsFalse() {
        // When a string of length `minLength - 1` is validated
        val result = validator("a".repeat(minLength - 1))
        // Then, it returns false
        assertFalse(result)
    }

    @Test
    fun minLengthValidator_minLengthString_returnTrue() {
        // When a string of length `minLength` is validated
        val result = validator("a".repeat(minLength))
        // Then, it returns true
        assertTrue(result)
    }

    @Test
    fun minLengthValidator_longString_returnsTrue() {
        // When a string of length `minLength * 100` is validated
        val result = validator("a".repeat(minLength * 100))
        // Then, it returns true
        assertTrue(result)
    }

}