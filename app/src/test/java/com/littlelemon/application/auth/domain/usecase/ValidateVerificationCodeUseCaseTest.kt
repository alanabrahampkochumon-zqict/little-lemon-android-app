package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.core.domain.utils.ValidationResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValidateVerificationCodeUseCaseTest {
    private lateinit var useCase: ValidateOTPUseCase

    @BeforeEach
    fun setUp() {
        useCase = ValidateOTPUseCase()
    }


    @Test
    fun givenEmptyOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = ""
        // Act
        val result = useCase(otp)
        // Assert
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenPartialOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = "123"
        // Act
        val result = useCase(otp)
        // Assert
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenLettersInOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = "abcd"
        // Act
        val result = useCase(otp)
        // Assert
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenWhitespaceInOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = "\t  "
        // Act
        val result = useCase(otp)
        // Assert
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenMoreThanFourDigitOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = "12345"
        // Act
        val result = useCase(otp)
        // Assert
        assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenFourNumberOTP_validatorReturns_success() = runTest {
        // Arrange
        val otp = "1234"
        // Act
        val result = useCase(otp)
        // Assert
        assertTrue(result is ValidationResult.Success)
    }
}