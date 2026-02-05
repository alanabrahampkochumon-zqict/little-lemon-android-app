package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.User
import com.littlelemon.application.auth.domain.usecase.params.VerificationParams
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VerifyVerificationCodeUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: VerifyOTPUseCase
    private lateinit var user: User

    private companion object {
        const val EMAIL = "test@email.com"
        const val OTP = "3316"
    }

    @BeforeEach
    fun setUp() {
        repository = mockk<AuthRepository>()
        useCase = VerifyOTPUseCase(repository)
        user = User(EMAIL)
    }


    @Test
    fun givenRepositoryReturnSuccess_useCaseReturn_successWithUser() = runTest {
        // Arrange
        coEvery { repository.verifyOTP(EMAIL, OTP) } returns Resource.Success()
        // Act
        val result = useCase(VerificationParams(EMAIL, OTP))

        // Assert
        assertTrue(result is Resource.Success)
    }

    @Test
    fun givenRepositoryReturnsFailure_useCaseReturn_failure() = runTest {
        // Arrange
        val message = "error"
        coEvery { repository.verifyOTP(EMAIL, OTP) } returns Resource.Failure(
            errorMessage = message
        )
        // Act
        val result = useCase(VerificationParams(EMAIL, OTP))

        // Assert
        assertTrue(result is Resource.Failure)
        assertEquals(message, (result as Resource.Failure).errorMessage)
    }
}