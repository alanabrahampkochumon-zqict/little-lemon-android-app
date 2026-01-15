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

class SendVerificationCodeUseCaseTest {

    private lateinit var repository: AuthRepository
    private lateinit var useCase: SendVerificationCodeUseCase
    private lateinit var email: String

    @BeforeEach
    fun setUp() {
        repository = mockk<AuthRepository>();
        useCase = SendVerificationCodeUseCase(repository)
        email = "test@email.com"
    }


    @Test
    fun givenRepositoryReturnSuccess_useCaseReturn_success() = runTest {
        // Arrange
        coEvery { repository.sendVerificationCode(email) } returns Resource.Success(
            Unit
        )
        // Act
        val result = useCase(email)

        // Assert
        assertTrue(result is Resource.Success)
        assertEquals(Unit, (result as Resource.Success).data)
    }

    @Test
    fun givenRepositoryReturnsFailure_useCaseReturn_failure() = runTest {
        // Arrange
        val message = "Unknown error"
        coEvery { repository.sendVerificationCode(email) } returns Resource.Failure(
            message
        )
        // Act
        val result = useCase(email)

        // Assert
        assertTrue(result is Resource.Failure)
        assertEquals(message, (result as Resource.Failure).errorMessage)
    }
}