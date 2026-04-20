package com.littlelemon.application.menu.data

import com.littlelemon.application.core.domain.utils.Error
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.domain.utils.toNetworkError
import com.littlelemon.application.menu.data.local.dao.MenuDao
import com.littlelemon.application.menu.data.local.models.DishWithCategories
import com.littlelemon.application.menu.data.mappers.toDish
import com.littlelemon.application.menu.data.mappers.toDishWithCategories
import com.littlelemon.application.menu.data.remote.MenuRemoteDataSource
import com.littlelemon.application.menu.domain.MenuRepository
import com.littlelemon.application.menu.domain.models.Category
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DefaultMenuRepository(
    private val localDataSource: MenuDao, private val remoteDataSource: MenuRemoteDataSource
) : MenuRepository {

    override fun getDishes(
        sorting: DishSorting,
        filter: DishFilter?,
        fetchFromRemote: Boolean,
        filterCategory: String?,
    ): Flow<Resource<List<Dish>>> = flow {

        emit(Resource.Loading())
        var dishesCount = localDataSource.getDishCount()

        if (fetchFromRemote || dishesCount < 1) {
            try {
                val dishDTOs = remoteDataSource.fetchDishes()

                if (dishDTOs.isNotEmpty()) {
                    val (dishes, categories, crossRefs) = dishDTOs.toDishWithCategories()
                    localDataSource.deleteAllDishes()
                    localDataSource.insertDishes(dishes, categories, crossRefs)
                }
            } catch (e: Exception) {
                val errorType = when (e) {
                    is PostgrestRestException -> e.code?.toIntOrNull()?.toNetworkError()
                        ?: Error.NetworkError.Unknown()

                    is HttpRequestTimeoutException -> Error.NetworkError.Timeout()
                    else -> Error.NetworkError.Unknown()
                }
                emit(Resource.Failure(data = null, errorMessage = e.message, error = errorType))
            }
        }
        dishesCount = localDataSource.getDishCount()
        if (dishesCount > 0) {
            val dbFlow = getDishesBySortingAndFiltering(sorting, filter).map { dishEntities ->
                if (!filterCategory.isNullOrBlank()) {
                    val dishes = dishEntities.map { dishWithCategories ->
                        if (filterCategory in dishWithCategories.categories.map { it.categoryName }) return@map dishWithCategories.toDish()
                        return@map null
                    }.filterNotNull()
                    Resource.Success(data = dishes)
                } else
                    Resource.Success(data = dishEntities.map { it.toDish() })
            }
            emitAll(dbFlow)
        } else {
            emit(
                Resource.Failure(
                    data = null,
                    errorMessage = "Unknown error occurred!",
                    error = Error.NetworkError.Unknown()
                )
            )
        }
    }

    override fun getAllCategories(): Flow<Resource<List<Category>>> = flow {
        try {
            emit(Resource.Loading())
            val categories = localDataSource.getAllCategories()
        } catch (e: Exception) {
            emit(Resource.Failure(errorMessage = MenuErrorMessages.CATEGORY_FETCH_FAILURE))
        }
    }

    private fun getDishesBySortingAndFiltering(
        sorting: DishSorting, filter: DishFilter?
    ): Flow<List<DishWithCategories>> {
        return when (sorting) {
            DishSorting.NAME_ASCENDING -> localDataSource.getDishesSortedByName(ascending = true)
            DishSorting.NAME_DESCENDING -> localDataSource.getDishesSortedByName(ascending = false)
            DishSorting.PRICE_ASCENDING -> localDataSource.getDishesSortedByPrice(ascending = true)
            DishSorting.PRICE_DESCENDING -> localDataSource.getDishesSortedByPrice(ascending = false)
            DishSorting.RECENTLY_ADDED -> localDataSource.getDishesSortedByAdded(ascending = false)
            DishSorting.POPULARITY -> localDataSource.getDishesSortedByPopularity()
            DishSorting.LOWEST_CALORIES -> localDataSource.getDishesSortedByCalories(ascending = true)
            DishSorting.HIGHEST_CALORIES -> localDataSource.getDishesSortedByCalories(ascending = false)
            DishSorting.HIGHEST_PROTEIN -> localDataSource.getDishesSortedByProtein(ascending = false)
        }.map { dishes ->
            dishes.filter { (dish, _) ->
                if (filter != null && filter == DishFilter.INCLUDE_OUT_OF_STOCK) true
                else dish.stock > 0
            }
        }
    }
}

