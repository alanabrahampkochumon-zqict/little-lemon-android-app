package com.littlelemon.application.address.domain.usecase

import com.littlelemon.application.address.domain.AddressRepository
import com.littlelemon.application.address.utils.AddressGenerator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class RemoveAddressUseCaseTest {
    private val repository: AddressRepository = mockk()
    private val useCase = RemoveAddressUseCase(repository)

    @Test
    fun callsRepositoryMethod() = runTest {
        val numAddress = 2
        val address = List(numAddress) { AddressGenerator.generateLocalAddress() }
        coEvery { repository.removeAddress(any()) } returns Unit

        // Act
        useCase(address[0])

        coVerify { repository.removeAddress(any()) }

    }
}