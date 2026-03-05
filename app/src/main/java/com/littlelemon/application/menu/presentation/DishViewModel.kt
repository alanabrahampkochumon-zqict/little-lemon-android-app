package com.littlelemon.application.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.R
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.domain.util.DishFilter
import com.littlelemon.application.menu.domain.util.DishSorting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DishViewModel(
    private val getDishes: GetDishesUseCase
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
    ) { filter, sorting, forceFetch -> Triple(filter, sorting, forceFetch) }
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
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DishState(isLoading = true))


    fun onAction(action: DishActions) {
        when (action) {
            is DishActions.ApplyFiltering -> TODO()
            is DishActions.ApplySorting -> TODO()
            is DishActions.FetchDishes -> TODO()
        }
    }

}