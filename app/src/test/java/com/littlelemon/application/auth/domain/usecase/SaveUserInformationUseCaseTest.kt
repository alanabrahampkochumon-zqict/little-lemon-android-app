package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.auth.domain.models.User
import com.littlelemon.application.auth.domain.usecase.params.UserInfoParams
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveUserInformationUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: SaveUserInformationUseCase
    private lateinit var userInformation: UserInfoParams
    private lateinit var user: User


    @BeforeEach
    fun setUp() {
        repository = mockk<AuthRepository>()
        useCase = SaveUserInformationUseCase(repository)
        userInformation = UserInfoParams(firstName = "John", lastName = "Doe")
        user = User(
            firstName = userInformation.firstName,
            lastName = userInformation.lastName,
            email = "user@test.com"
        )
    }

    @Test
    fun givenRepositoryReturnFailure_useCaseReturn_failure() = runTest {
        // Arrange
        val message = "error"
        coEvery {
            repository.saveUserInformation(
                userInformation.firstName,
                userInformation.lastName
            )
        } returns Resource.Failure(message)

        // Act
        val result = useCase(userInformation)
        // Assert
        assertTrue(result is Resource.Failure)
        assertEquals(message, (result as Resource.Failure).errorMessage)
    }

    @Test
    fun givenRepositoryReturnSuccess_useCaseReturn_successWithUserInformation() = runTest {
        // Arrange
        coEvery {
            repository.saveUserInformation(
                userInformation.firstName,
                userInformation.lastName
            )
        } returns Resource.Success()

        // Act
        val result = useCase(userInformation)
        // Assert
        assertTrue(result is Resource.Success)
    }


}