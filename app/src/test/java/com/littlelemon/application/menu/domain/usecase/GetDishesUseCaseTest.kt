package com.littlelemon.application.menu.domain.usecase

import androidx.test.filters.SmallTest
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.data.mappers.toDish
import com.littlelemon.application.menu.domain.MenuRepository
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
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
import kotlin.test.assertTrue

@SmallTest
class GetDishesUseCaseTest {

    val dishes = DishGenerator.generateDishWithCategories(5).map { it.first }

    private lateinit var repository: MenuRepository
    private lateinit var useCase: GetDishesUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        coEvery { repository.getDishes() } returns flow { emit(Resource.Success(dishes.map { it.toDish() })) }
        coEvery { repository.getDishes(DishSorting.HIGHEST_PROTEIN) } returns flow {
            emit(
                Resource.Success(
                    dishes.sortedByDescending { it.dish.nutritionInfo?.protein }
                        .map { it.toDish() })
            )
        }
        useCase = GetDishesUseCase(repository)
    }

    @Test
    fun getDishes_remoteSuccess_returnsDishes() = runTest {
        // Given the repository returns remote success

        // When the dishes are queried
        val result = useCase().first()

        // Then, the resource is success, with correct data
        assertIs<Resource.Success<List<Dish>>>(result)
        assertNotNull(result.data)
        assertEquals(dishes.map { it.toDish() }, result.data)
    }

    @Test
    fun getDishes_remoteSuccessWithSortedByProtein_returnsDishesSortedByProtein() = runTest {
        // Given the repository returns remote success

        // When the dishes are queried
        val result = useCase(DishSorting.HIGHEST_PROTEIN).first()

        // Then, the resource is success, with data sorted by highest protein
        assertIs<Resource.Success<List<Dish>>>(result)
        assertNotNull(result.data)
        assertTrue(result.data.zipWithNext { firstDish, secondDish ->
            (firstDish.nutritionInfo?.protein ?: 0) >= (secondDish.nutritionInfo?.protein ?: 0)
        }
            .all { it })
    }

    @Test
    fun getDishes_remoteSuccessWithIncludeOutOfStock_returnDishesWithOutOfStock() = runTest {
        // Given the repository returns remote success
        val copied =
            dishes.map { it.toDish() }
                .map { it.copy(stock = if (Math.random() > 0.5) 0 else it.stock) }
        val outOfStockDishes = copied.count { it.stock <= 0 }
        coEvery { repository.getDishes(filter = DishFilter.INCLUDE_OUT_OF_STOCK) } returns flow {
            emit(
                Resource.Success(
                    copied
                )
            )
        }

        // When the dishes are queried
        val result = useCase(filter = DishFilter.INCLUDE_OUT_OF_STOCK).first()

        // Then, the resource is success, with data sorted by highest protein
        assertIs<Resource.Success<List<Dish>>>(result)
        assertNotNull(result.data)
        assertEquals(outOfStockDishes, result.data.count { it.stock == 0 })
    }

    @Test
    fun getDishes_remoteFailure_returnResourceFailure() = runTest {
        // When the repository returns a remote failure
        coEvery { repository.getDishes() } returns flow { emit(Resource.Failure()) }

        // Then usecase also returns a failure
        val result = useCase().first()
        assertIs<Resource.Failure<List<Dish>>>(result)
    }

    @Test
    fun getDishes_forceFetch_getsNewDishes() = runTest {
        val newDishes = DishGenerator.generateDishWithCategories(10)
            .map { (dishEntity, _) -> dishEntity.toDish() }
        coEvery { repository.getDishes(fetchFromRemote = true) } returns flow {
            emit(
                Resource.Success(
                    newDishes
                )
            )
        }

        // When dishes are forceFetched
        val result = useCase(forceFetch = true).first()

        // Then, the resource is success, with correct data
        assertIs<Resource.Success<List<Dish>>>(result)
        assertNotNull(result.data)
        assertEquals(newDishes, result.data)
    }

}