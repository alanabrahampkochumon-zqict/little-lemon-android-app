package com.littlelemon.application.menu.domain.usecase

import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.data.mappers.toDomainCategory
import com.littlelemon.application.menu.domain.MenuRepository
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.utils.DishGenerator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetCategoriesUseCaseTest {


    private lateinit var repository: MenuRepository
    private lateinit var useCase: GetCategoriesUseCase
    private val categories = DishGenerator.generateCategoryEntities().map { it.toDomainCategory() }

    @BeforeEach
    fun setUp() {
        repository = mockk()
        coEvery { repository.getAllCategories() } returns flow {
            emit(Resource.Success(categories))
        }
        useCase = GetCategoriesUseCase(repository)
    }

    @Test
    fun repositorySuccess_returnsSuccessWithData() = runTest {
        val result = useCase().first()

        assertIs<Resource.Success<List<Category>>>(result)
        assertNotNull(result.data)
        assertEquals(categories, result.data)
    }

    @Test
    fun repositoryFailure_returnsFailure() = runTest {
        coEvery { repository.getAllCategories() } returns flow {
            emit(Resource.Failure())
        }
        useCase = GetCategoriesUseCase(repository)

        val result = useCase().first()
        assertIs<Resource.Failure<List<Category>>>(result)
    }
}