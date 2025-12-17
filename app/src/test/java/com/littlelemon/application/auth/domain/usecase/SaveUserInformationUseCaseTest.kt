package com.littlelemon.application.auth.domain.usecase

import com.littlelemon.application.auth.domain.AuthRepository
import com.littlelemon.application.core.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before

class SaveUserInformationUseCaseTest {
    private lateinit var repository: AuthRepository
    private lateinit var useCase: SaveUserInformationUseCase

    @Before
    fun setUp() {
        repository = mockk<AuthRepository>();
        useCase = SaveUserInformationUseCase(repository)
    }

    fun givenEmptyFirstName_useCaseReturn_error() {
        // Arrange
        val userInformation = UserInformation(firstName = "", lastName = "lastname")
        val message = "error"
        coEvery {
            repository.saveUserInformation(
                userInformation.firstName,
                userInformation.lastName
            )
        } returns Resource.Failure(message)

//        val
    }

}