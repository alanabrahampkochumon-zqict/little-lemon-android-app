package com.littlelemon.application.auth.data

import com.littlelemon.application.auth.data.local.AuthLocalDataSource
import com.littlelemon.application.auth.data.remote.AuthRemoteDataSource
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AuthRepositoryImplTest {

    private companion object {
        const val EMAIL_ADDRESS = "test@email.com"
    }

    private val remoteDataSource = mockk<AuthRemoteDataSource>()
    private val localDataSource= mockk<AuthLocalDataSource>()

    private val repository = AuthRepositoryImpl(
        remoteDataSource,
        localDataSource
    )

    @Test
    fun sendVerificationCode_remoteSuccess_returnsResourceSuccess() = runTest {
        // Arrange
        coEvery { remoteDataSource.sendVerificationCode(EMAIL_ADDRESS) } returns Unit

        // Act
        val result = repository.sendVerificationCode(EMAIL_ADDRESS)

        // Assert
        assertTrue(result is Resource.Success)

        // Verify Remote Function is called
        coVerify(exactly = 1) { remoteDataSource.sendVerificationCode(EMAIL_ADDRESS) }
    }

    @Test
    fun sendVerificationCode_remoteFailure_returnResourceError() = runTest {
        // Arrange
        val expectedError = "Network Error"
        coEvery { remoteDataSource.sendVerificationCode(EMAIL_ADDRESS) } throws Exception(expectedError)

        // Act
        val result = repository.sendVerificationCode(EMAIL_ADDRESS)

        // Assert
        assertTrue(result is Resource.Failure)
        result as Resource.Failure
        assertEquals(expectedError, result.errorMessage)
    }

}