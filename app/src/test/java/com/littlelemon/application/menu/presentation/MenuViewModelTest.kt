package com.littlelemon.application.menu.presentation

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.menu.utils.DishGenerator
import com.littlelemon.application.shared.cart.domain.models.CartItem
import com.littlelemon.application.shared.cart.domain.usecase.GetCartItemUseCase
import com.littlelemon.application.shared.menu.data.mappers.toDish
import com.littlelemon.application.shared.menu.domain.usecase.GetCategoriesUseCase
import com.littlelemon.application.shared.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.shared.menu.domain.util.DishFilter
import com.littlelemon.application.shared.menu.domain.util.DishSorting
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@SmallTest
@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(StandardTestDispatcherRule::class)
class MenuViewModelTest {

    private val dishes =
        DishGenerator.generateDishWithCategories(10).map { (dishEntity, _) -> dishEntity.toDish() }

    val cartItems = dishes.drop(5).map { dish -> CartItem(dish.id, Random.nextInt(3, 5)) }

    private val categories = dishes.flatMap { it.category }.distinct()
    private val outOfStockDishes = DishGenerator.generateDishWithCategories(5)
        .map { (dishEntity, _) -> dishEntity.toDish().copy(stock = 0) }
    private val remoteDishes =
        DishGenerator.generateDishWithCategories(15).map { (dishEntity, _) -> dishEntity.toDish() }
    private lateinit var getDishesUseCase: GetDishesUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var getCartItemUseCase: GetCartItemUseCase
    private lateinit var viewModel: MenuViewModel

    @BeforeEach
    fun setUp() {
        getDishesUseCase = mockk()
        getCategoriesUseCase = mockk()
        getCartItemUseCase = mockk()

        coEvery { getDishesUseCase.invoke() } returns flow {
            emit(Resource.Success(dishes))
        }
        coEvery { getCategoriesUseCase.invoke() } returns flow {
            emit(Resource.Success(categories))
        }
        coEvery { getCartItemUseCase.invoke() } returns flow {
            emit(cartItems)
        }

        viewModel = MenuViewModel(getDishesUseCase, getCategoriesUseCase, getCartItemUseCase)
    }


    @Nested
    inner class ViewModelInitialization {

        @Nested
        inner class MenuState {
            @Test
            fun initialStateIsSetToLoading() = runTest {
                // Given viewmodel is created
                viewModel.state.test {
                    // Then, initial state is loading
                    assertTrue(awaitItem().isLoading)
                }

            }

            @Test
            fun getsDishesFromUseCase() = runTest {
                // Given viewmodel is created
                viewModel.state.test {
                    awaitItem() // Skips the initial loading

                    // Then, then result is populated
                    val state = awaitItem()

                    assertFalse(state.isLoading)
                    assertNull(state.error)
                    assertEquals(dishes, state.dishes?.map { it.dish })
                }
            }
        }


        @Nested
        inner class Categories {

            @Test
            fun initialStateIsLoading() = runTest {
                viewModel.categories.test {
                    assertTrue { awaitItem().isLoading }
                }
            }


            @Test
            fun getsCategoriesFromUseCase() = runTest {
                viewModel.categories.test {
                    awaitItem() // Skip initial state
                    val state = awaitItem()
                    assertEquals(categories, state.categories)
                    assertNull(state.error)
                    assertFalse(state.isLoading)
                }
            }

            @Test
            fun useCaseError() = runTest {
                coEvery { getCategoriesUseCase.invoke() } returns flow {
                    emit(Resource.Failure())
                }
                viewModel =
                    MenuViewModel(getDishesUseCase, getCategoriesUseCase, getCartItemUseCase)

                viewModel.categories.test {
                    awaitItem() // Skip initial state
                    val state = awaitItem()
                    assertNotNull(state.error)
                    assertFalse { state.isLoading }
                }
            }
        }
    }


    @Nested
    inner class Actions {

        @Nested
        inner class ApplyFilter {

            @BeforeEach
            fun setUp() {
                coEvery { getDishesUseCase.invoke(filter = DishFilter.INCLUDE_OUT_OF_STOCK) } returns flow {
                    emit(
                        Resource.Success(dishes + outOfStockDishes)
                    )
                }
                coEvery { getDishesUseCase.invoke(filter = null) } returns flow {
                    emit(Resource.Success(dishes))
                }
                viewModel =
                    MenuViewModel(getDishesUseCase, getCategoriesUseCase, getCartItemUseCase)
            }

            @Test
            fun outOfStockFilter_emitsDishesIncludingOutOfStock() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When, include out of stock filter is applied
                    viewModel.onAction(MenuActions.ApplyFiltering(filter = DishFilter.INCLUDE_OUT_OF_STOCK))

                    // Then, the result contains dishes including out of stock dishes
                    val state = awaitItem()
                    assertEquals(dishes + outOfStockDishes, state.dishes?.map { it.dish })
                }
            }

            @Test
            fun noFilterApplied_emitsOnlyInStockDishes() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When, include out of stock filter is applied
                    viewModel.onAction(MenuActions.ApplyFiltering(filter = null))

                    // Then, the result contains dishes including out of stock dishes
                    val state = awaitItem()
                    assertEquals(dishes, state.dishes?.map { it.dish })
                }
            }
        }

        @Nested
        inner class ApplySorting {

            @BeforeEach
            fun setUp() {
                coEvery { getDishesUseCase.invoke(sorting = DishSorting.NAME_ASCENDING) } returns flow {
                    emit(
                        Resource.Success(dishes.sortedBy { dish -> dish.title })
                    )
                }
                coEvery { getDishesUseCase.invoke(sorting = DishSorting.NAME_DESCENDING) } returns flow {
                    emit(
                        Resource.Success(dishes.sortedByDescending { dish -> dish.title })
                    )
                }
                viewModel =
                    MenuViewModel(getDishesUseCase, getCategoriesUseCase, getCartItemUseCase)
            }


            @Test
            fun sortedByNameAscending_emitsDishesSortedByNameAscending() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When, sort by name ascending is applied
                    viewModel.onAction(MenuActions.ApplySorting(DishSorting.NAME_ASCENDING))

                    // Then, the result contains dishes sorted by name ascending
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertTrue(state.dishes.map { it.dish }
                        .zipWithNext { firstDish, secondDish -> firstDish.title <= secondDish.title }
                        .all { it })
                    assertFalse(state.isLoading) // and isLoading is false
                }
            }

            @Test
            fun sortedByNameDescending_emitsDishesSortedByNameAscending() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When, sort by name ascending is applied
                    viewModel.onAction(MenuActions.ApplySorting(DishSorting.NAME_DESCENDING))

                    // Then, the result contains dishes sorted by name ascending
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertTrue(state.dishes.map { it.dish }
                        .zipWithNext { firstDish, secondDish -> firstDish.title >= secondDish.title }
                        .all { it })
                    assertFalse(state.isLoading) // and isLoading is false
                }
            }
        }

        @Nested
        inner class FetchDishes {

            @BeforeEach
            fun setUp() {
                coEvery { getDishesUseCase.invoke(forceFetch = true) } returns flow {
                    emit(
                        Resource.Success(remoteDishes)
                    )
                }

                coEvery { getDishesUseCase.invoke(forceFetch = false) } returns flow {
                    emit(
                        Resource.Success(dishes)
                    )
                }
                viewModel =
                    MenuViewModel(getDishesUseCase, getCategoriesUseCase, getCartItemUseCase)
            }

            @Test
            fun withFromRemote_fetchesItemFromRemote() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When forceFetch is called
                    viewModel.onAction(MenuActions.FetchDishes(true))

                    // Then, items are fetched from remote
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertEquals(remoteDishes, state.dishes.map { it.dish })
                }
            }

            @Test
            fun withoutFromRemote_fetchesItemFromCache() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When forceFetch is called
                    viewModel.onAction(MenuActions.FetchDishes(false))

                    // Then, items are fetched from cache
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertEquals(dishes, state.dishes.map { it.dish })
                }
            }
        }

        @Nested
        inner class UpdateCategory {
            private val filterCategory = "CATEGORY"
            private val categoryFilteredDishes = DishGenerator.generateDishWithCategories(5)
                .map { (dishEntity, _) -> dishEntity.toDish() }

            @BeforeEach
            fun setUp() {
                coEvery { getDishesUseCase.invoke(filterCategory = null) } returns flow {
                    emit(Resource.Success(dishes))
                }

                coEvery { getDishesUseCase.invoke(filterCategory = filterCategory) } returns flow {
                    emit(Resource.Success(categoryFilteredDishes))
                }

                viewModel =
                    MenuViewModel(getDishesUseCase, getCategoriesUseCase, getCartItemUseCase)
            }


            @Test
            fun nullCategory_returnsAllDishes() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When UpdateDishCategoryAction is triggered null category
                    viewModel.onAction(MenuActions.UpdateDishCategory(null))

                    // Then, the original dishes are emitted
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertEquals(dishes, state.dishes.map { it.dish })
                }
            }

            @Test
            fun validCategory_returnsFilteredDishes() = runTest {
                viewModel.state.test {
                    awaitItem() // Skip the initial loading

                    // When UpdateDishCategoryAction is triggered null category
                    viewModel.onAction(MenuActions.UpdateDishCategory(filterCategory))

                    // Then, the filtered dishes are emitted
                    val state = awaitItem()
                    assertNotNull(state.dishes)
                    assertEquals(
                        categoryFilteredDishes,
                        state.dishes.map { it.dish })
                }
            }
        }
    }

}