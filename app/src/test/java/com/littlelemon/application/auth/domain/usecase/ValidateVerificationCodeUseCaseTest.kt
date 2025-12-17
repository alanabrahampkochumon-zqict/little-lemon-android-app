package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ValidateVerificationCodeUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: ValidateVerificationCodeUseCase
    private lateinit var otp: String

    @Before
    fun setUp() {
        repository = mockk<AuthRepository>();
        useCase = ValidateVerificationCodeUseCase(repository)
        otp = "3316"
    }


    @Test
    fun givenRepositoryReturnSuccess_useCaseReturn_success() {
        // Arrange
        coEvery { repository.validateVerificationCode(otp) } returns Resource.Success(
            Unit
        )
        // Act
        val result = useCase(otp)

        // Assert
        Assert.assertTrue(result is Resource.Success)
        Assert.assertEquals(Unit, (result as Resource.Success).data)
    }

    @Test
    fun givenRepositoryReturnsFailure_useCaseReturn_failure() {
        // Arrange
        val message = "error"
        coEvery { repository.validateVerificationCode(otp) } returns Resource.Failure(
            message
        )
        // Act
        val result = useCase(otp)

        // Assert
        Assert.assertTrue(result is Resource.Failure)
        Assert.assertEquals(message, (result as Resource.Failure).errorMessage)
    }
}