package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.core.domain.utils.ValidationResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class ValidateFirstNameUseCaseTest {
    private lateinit var useCase: ValidateFirstNameUseCase

    @Before
    fun setUp() {
        useCase = ValidateFirstNameUseCase()
    }

    @Test
    fun givenEmptyFirstName_validatorReturns_failure() = runTest {
        // Arrange
        val firstName = ""

        // Act
        val result = useCase(firstName)

        // Assert
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenSingleLetterFirstName_validatorReturns_failure() = runTest {
        // Arrange
        val firstName = "A"

        // Act
        val result = useCase(firstName)

        // Assert
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenCorrectFirstName_validatorReturns_success() = runTest {
        // Arrange
        val firstName = "John"

        // Act
        val result = useCase(firstName)

        // Assert
        assertTrue(result is ValidationResult.Success)
    }

}