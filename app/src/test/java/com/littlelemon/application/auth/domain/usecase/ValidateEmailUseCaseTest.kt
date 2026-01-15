package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.core.domain.utils.ValidationResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValidateEmailUseCaseTest {
    private lateinit var validEmails: List<String>
    private lateinit var invalidEmails: List<String>

    private lateinit var useCase: ValidateEmailUseCase

    @BeforeEach
    fun setUp() {
        validEmails = listOf("test@example.com", "user@domain.com", "user@yahoo.com")
        invalidEmails = listOf("test", "email.com", "")
        useCase = ValidateEmailUseCase()
    }


    @Test
    fun givenValidEmail_ValidatorReturns_success() = runTest {
        for (email in validEmails) {
            val result = useCase(email)
            assertTrue(result is ValidationResult.Success)
        }
    }

    @Test
    fun givenInvalidEmail_ValidatorReturns_failure() = runTest {
        for (email in invalidEmails) {
            val result = useCase(email)
            assertTrue(result is ValidationResult.Failure)
        }
    }

    @Test
    fun givenEmptyEmail_ValidatorReturns_failure() = runTest {
        val emptyEmail = ""
        val result = useCase(emptyEmail)
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenVeryLongEmail_ValidatorReturns_failure() = runTest {
        val email = "a".repeat(250) + "@email.com"
        val result = useCase(email)
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun givenEmailWithLeadingSpaces_ValidatorReturns_failure() = runTest {
        val result = useCase("\t    validemail@email.com    ")
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenWhitespaces_ValidatorReturns_failure() = runTest {
        val result = useCase("\t   \n")
        assertTrue(result is ValidationResult.Failure)
    }
}