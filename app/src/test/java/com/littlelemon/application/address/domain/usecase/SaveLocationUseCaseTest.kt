package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.utils.AddressGenerator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

class SaveLocationUseCaseTest {

    private lateinit var repository: AddressRepository
    private lateinit var useCase: SaveLocationUseCase

    private val address = AddressGenerator.generateLocalAddress()

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = SaveLocationUseCase(repository)
    }

    @Test
    fun onUseCaseInvoke_repositorySuccess_returnsRepositorySuccess() = runTest {
        // Arrange
        coEvery { repository.saveAddress(address) } returns Resource.Success()

        // Act
        val result = useCase(address)

        // Assert
        assertIs<Resource.Success<Unit>>(result)
    }

    @Test
    fun onUseCaseInvoke_repositoryFailure_returnsRepositoryFailure() = runTest {
        // Arrange
        coEvery { repository.saveAddress(address) } returns Resource.Failure()

        // Act
        val result = useCase(address)

        // Assert
        assertIs<Resource.Failure<Unit>>(result)
    }
}