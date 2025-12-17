package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.core.domain.utils.ValidationResult
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateEmailUseCaseTest {
    private val validEmails = listOf("test@example.com", "user@domain.com", "user@yahoo.com")
    private val invalidEmails = listOf("test", "email.com", "")

    private val validateEmail = ValidateEmailUseCase()

    @Test
    fun givenValidEmail_ValidatorReturns_Success() {
        for (email in validEmails) {
            val result = validateEmail(email)
            assertTrue(result is ValidationResult.Success)
        }
    }

    @Test
    fun givenInvalidEmail_ValidatorReturns_Failure() {
        for (email in invalidEmails) {
            val result = validateEmail(email)
            assertTrue(result is ValidationResult.Failure)
        }
    }

    @Test
    fun givenEmptyEmail_ValidatorReturns_Failure() {
        val emptyEmail = ""
        val result = validateEmail(emptyEmail)
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenVeryLongEmail_ValidatorReturns_Failure() {
        val email = "a".repeat(250) + "@email.com"
        val result = validateEmail(email)
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun givenEmailWithLeadingSpaces_ValidatorReturns_Failure() {
        val result = validateEmail("\t    validemail@email.com    ")
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenWhitespaces_ValidatorReturns_Failure() {
        val result = validateEmail("\t   \n")
        assertTrue(result is ValidationResult.Failure)
    }
}