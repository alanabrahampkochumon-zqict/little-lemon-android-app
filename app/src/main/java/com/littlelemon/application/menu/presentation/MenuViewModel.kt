package com.littlelemon.application.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.R
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.menu.domain.usecase.GetCategoriesUseCase
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val getDishes: GetDishesUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(
) {
    private val _filterFlow = MutableStateFlow<DishFilter?>(null)
    private val _dishSortingFlow = MutableStateFlow(DishSorting.POPULARITY)
    private val _forceFetch = MutableStateFlow(false)


    private val _currentCategory = MutableStateFlow<String?>(null)
    val currentCategory = _currentCategory.asStateFlow()


    val categories = getCategories().map { resource ->
        when (resource) {
            is Resource.Failure -> CategoryState(
                isLoading = false,
                error = if (resource.errorMessage != null) UiText.DynamicString(resource.errorMessage) else UiText.StringResource(
                    R.string.category_fetch_unknown_error
                )
            )

            is Resource.Loading -> CategoryState(isLoading = true, error = null)

            is Resource.Success -> CategoryState(
                isLoading = false, error = null, categories = resource.data ?: emptyList()
            )
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), CategoryState(isLoading = true)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = combine(
        _dishSortingFlow, _filterFlow, _forceFetch, _currentCategory
    ) { sorting, filtering, forceFetch, currentCategory ->
        DishDataParams(
            sorting, filtering, forceFetch, currentCategory
        )
    }.flatMapLatest { (sorting, filter, forceFetch, currentCategory) ->
            getDishes(sorting, filter, forceFetch, currentCategory)
        }.map { resource ->
            when (resource) {
                is Resource.Failure -> MenuState(
                    dishes = resource.data,
                    isLoading = false,
                    error = if (resource.errorMessage != null) UiText.DynamicString(resource.errorMessage) else UiText.StringResource(
                        R.string.generic_error_message
                    )
                )

                is Resource.Loading -> MenuState(isLoading = true, error = null)
                is Resource.Success -> MenuState(
                    dishes = resource.data, isLoading = false, error = null
                )
            }
        }.flowOn(ioDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MenuState(isLoading = true))

    fun onAction(action: MenuActions) {
        when (action) {
            is MenuActions.ApplyFiltering -> _filterFlow.update { action.filter }
            is MenuActions.ApplySorting -> _dishSortingFlow.update { action.sorting }
            is MenuActions.FetchDishes -> _forceFetch.update { action.fromRemote }
            is MenuActions.UpdateDishCategory -> _currentCategory.update { action.category }
        }
    }

}