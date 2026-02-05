package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ResendOTPUseCaseTest {

    private lateinit var repository: AuthRepository
    private lateinit var useCase: ResendOTPUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk<AuthRepository>()
        useCase = ResendOTPUseCase(repository)
    }

    private val email = "email@test.com"

    @Test
    fun onOTPResend_repositoryReturnsSuccess_returnsSuccess() = runTest {
        // Given
        coEvery { repository.resendOTP(email) } returns Resource.Success()

        // Act
        val result = useCase(email)

        // Assert
        assertTrue(result is Resource.Success)
    }

    @Test
    fun onOTPResend_repositoryReturnsFailure_returnFailure() = runTest {
        // Given
        val errorMessage = "Error Message"
        coEvery { repository.resendOTP(email) } returns Resource.Failure(errorMessage = errorMessage)

        // Act
        val result = useCase(email)

        // Assert
        assertTrue(result is Resource.Failure)
        result as Resource.Failure
        assertEquals(errorMessage, result.errorMessage)
    }

}