package com.littlelemon.application.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.R
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DishViewModel(
    private val getDishes: GetDishesUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(
) {
    private val _filterFlow = MutableStateFlow<DishFilter?>(null)
    private val _dishSortingFlow = MutableStateFlow(DishSorting.POPULARITY)
    private val _forceFetch = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = combine(
        _dishSortingFlow,
        _filterFlow,
        _forceFetch
    ) { sorting, filtering, forceFetch -> Triple(sorting, filtering, forceFetch) }
        .flatMapLatest { (sorting, filter, forceFetch) ->
            getDishes(sorting, filter, forceFetch)
        }.map { resource ->
            when (resource) {
                is Resource.Failure -> DishState(
                    dishes = resource.data,
                    isLoading = false,
                    error = if (resource.errorMessage != null) UiText.DynamicString(resource.errorMessage) else UiText.StringResource(
                        R.string.generic_error_message
                    )
                )

                is Resource.Loading -> DishState(isLoading = true, error = null)
                is Resource.Success -> DishState(
                    dishes = resource.data,
                    isLoading = false,
                    error = null
                )
            }
        }
        .flowOn(ioDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DishState(isLoading = true))


    fun onAction(action: DishActions) {
        when (action) {
            is DishActions.ApplyFiltering -> _filterFlow.update { action.filter }
            is DishActions.ApplySorting -> _dishSortingFlow.update { action.sorting }
            is DishActions.FetchDishes -> TODO()
        }
    }

}