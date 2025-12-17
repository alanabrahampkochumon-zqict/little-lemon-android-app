package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.User
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class VerifyVerificationCodeUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: VerifyVerificationCodeUseCase
    private lateinit var otp: String
    private lateinit var user: User

    @Before
    fun setUp() {
        repository = mockk<AuthRepository>()
        useCase = VerifyVerificationCodeUseCase(repository)
        otp = "3316"
        user = User("test@email.com")
    }


    @Test
    fun givenRepositoryReturnSuccess_useCaseReturn_successWithUser() = runTest {
        // Arrange
        coEvery { repository.verifyVerificationCode(otp) } returns Resource.Success(
            user
        )
        // Act
        val result = useCase(otp)

        // Assert
        Assert.assertTrue(result is Resource.Success)
        Assert.assertEquals(user, (result as Resource.Success).data)
    }

    @Test
    fun givenRepositoryReturnsFailure_useCaseReturn_failure() = runTest {
        // Arrange
        val message = "error"
        coEvery { repository.verifyVerificationCode(otp) } returns Resource.Failure(
            message
        )
        // Act
        val result = useCase(otp)

        // Assert
        Assert.assertTrue(result is Resource.Failure)
        Assert.assertEquals(message, (result as Resource.Failure).errorMessage)
    }
}