package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.core.domain.utils.ValidationResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ValidateVerificationCodeUseCaseTest {
    private lateinit var useCase: ValidateVerificationCodeUseCase

    @Before
    fun setUp() {
        useCase = ValidateVerificationCodeUseCase()
    }


    @Test
    fun givenEmptyOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = ""
        // Act
        val result = useCase(otp)
        // Assert
        Assert.assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenPartialOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = "123"
        // Act
        val result = useCase(otp)
        // Assert
        Assert.assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenLettersInOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = "abcd"
        // Act
        val result = useCase(otp)
        // Assert
        Assert.assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenWhitespaceInOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = "\t  "
        // Act
        val result = useCase(otp)
        // Assert
        Assert.assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenMoreThanFourDigitOTP_validatorReturns_failure() = runTest {
        // Arrange
        val otp = "12345"
        // Act
        val result = useCase(otp)
        // Assert
        Assert.assertTrue(result is ValidationResult.Failure)
    }

    @Test
    fun givenFourNumberOTP_validatorReturns_success() = runTest {
        // Arrange
        val otp = "1234"
        // Act
        val result = useCase(otp)
        // Assert
        Assert.assertTrue(result is ValidationResult.Success)
    }
}