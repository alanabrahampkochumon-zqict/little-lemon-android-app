package com.littlelemon.application.shared.cart.domain.usecase

import com.littlelemon.application.cart.data.FakeCartRepository
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.shared.cart.domain.CartRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

class RefreshCartUseCaseTest {
    private lateinit var repository: CartRepository

    private lateinit var useCase: RefreshCartUseCase

    @Test
    fun repositorySuccess_returnsSuccess() = runTest {
        repository = FakeCartRepository()
        useCase = RefreshCartUseCase(repository)

        val status = useCase()
        assertIs<Resource.Success<Unit>>(status)
    }

    @Test
    fun repositoryFailure_returnsFailure() = runTest {
        repository = FakeCartRepository(throwError = true)
        useCase = RefreshCartUseCase(repository)

        val status = useCase()
        assertIs<Resource.Failure<Unit>>(status)
    }
}