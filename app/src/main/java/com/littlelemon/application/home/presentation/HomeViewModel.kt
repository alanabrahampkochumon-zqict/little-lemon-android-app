package com.littlelemon.application.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.menu.domain.usecase.GetCategoriesUseCase
import com.littlelemon.application.menu.domain.usecase.GetDishesUseCase
import com.littlelemon.application.menu.domain.util.DishSorting
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    getAddress: GetAddressUseCase,
    getDishes: GetDishesUseCase,
    getCategories: GetCategoriesUseCase,
) : ViewModel() {

    val state = combine(
        getAddress(),
        getDishes(sorting = DishSorting.POPULARITY),
        getCategories()
    ) { addressResult, dishResult, categoryResult ->
        var state = HomeState(
            dishLoading = true,
            addressLoading = true,
            categoryLoading = true
        )
        state = when (addressResult) {
            is Resource.Failure -> state.copy(
                addressLoading = false,
                addressError = if (addressResult.errorMessage != null) UiText.DynamicString(
                    addressResult.errorMessage
                ) else UiText.StringResource(R.string.address_loading_error_message)
            )

            is Resource.Loading -> state.copy(addressLoading = true, addressError = null)
            is Resource.Success -> state.copy(
                addressLoading = false,
                addressError = null,
                addresses = addressResult.data ?: emptyList()
            )
        }

        state = when (dishResult) {
            is Resource.Failure -> state.copy(
                dishLoading = false,
                dishError = if (dishResult.errorMessage != null) UiText.DynamicString(
                    dishResult.errorMessage
                ) else UiText.StringResource(R.string.dish_loading_error_message)
            )

            is Resource.Loading -> state.copy(dishLoading = true, dishError = null)
            is Resource.Success -> state.copy(
                dishLoading = false,
                dishError = null,
                popularDishes = dishResult.data ?: emptyList()
            )
        }

        state = when (categoryResult) {
            is Resource.Failure -> state.copy(
                categoryLoading = false,
                categoryError = if (categoryResult.errorMessage != null) UiText.DynamicString(
                    categoryResult.errorMessage
                ) else UiText.StringResource(R.string.address_loading_error_message)
            )

            is Resource.Loading -> state.copy(categoryLoading = true, categoryError = null)
            is Resource.Success -> state.copy(
                categoryLoading = false,
                categoryError = null,
                categories = categoryResult.data ?: emptyList()
            )
        }
        return@combine state
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            Resource.Loading(
                HomeState(
                    dishLoading = true,
                    addressLoading = true,
                    categoryLoading = true
                )
            )
        )


}