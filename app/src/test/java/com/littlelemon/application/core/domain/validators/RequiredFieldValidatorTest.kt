package com.littlelemon.application.core.domain.validators

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class RequiredFieldValidatorTest {

    private val validator = RequiredFieldValidator()

    @Test
    fun requiredFieldValidator_emptyString_returnsFalse() {
        // When an empty string is validated
        val result = validator("")
        // Then, it returns false
        assertFalse(result)
    }

    @Test
    fun requiredFieldValidator_emptySpace_returnsFalse() {
        // When a string with empty space is validated
        val result = validator("     ")
        // Then, it returns false
        assertFalse(result)
    }

    @Test
    fun requiredFieldValidator_nonEmptyString_returnsTrue() {
        // When a string with value is validated
        val result = validator("value")
        // Then, it returns true
        assertTrue(result)
    }
}