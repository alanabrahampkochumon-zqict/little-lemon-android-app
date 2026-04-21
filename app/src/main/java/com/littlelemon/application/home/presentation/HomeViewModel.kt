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

        ///// ADDRESS //////
        val addressLoading = addressResult is Resource.Loading
        val addressError = if (addressResult is Resource.Failure)
            if (addressResult.errorMessage != null)
                UiText.DynamicString(
                    addressResult.errorMessage
                )
            else UiText.StringResource(R.string.address_loading_error_message)
        else null
        val addresses =
            if (addressResult is Resource.Success && addressResult.data != null) addressResult.data else emptyList()


        ///// DISHES //////
        val dishLoading = dishResult is Resource.Loading
        val dishError = if (dishResult is Resource.Failure)
            if (dishResult.errorMessage != null)
                UiText.DynamicString(
                    dishResult.errorMessage
                )
            else UiText.StringResource(R.string.dish_loading_error_message)
        else null
        val dishes =
            if (dishResult is Resource.Success && dishResult.data != null) dishResult.data else emptyList()


        ///// CATEGORIES //////
        val categoryLoading = categoryResult is Resource.Loading
        val categoryError = if (categoryResult is Resource.Failure)
            if (categoryResult.errorMessage != null)
                UiText.DynamicString(
                    categoryResult.errorMessage
                )
            else UiText.StringResource(R.string.category_loading_error_message)
        else null
        val categories =
            if (categoryResult is Resource.Success && categoryResult.data != null) categoryResult.data else emptyList()

        return@combine HomeState(
            dishLoading = dishLoading,
            dishError = dishError,
            popularDishes = dishes,
            addressLoading = addressLoading,
            addressError = addressError,
            addresses = addresses,
            categoryLoading = categoryLoading,
            categoryError = categoryError,
            categories = categories
        )
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeState(
                dishLoading = true,
                addressLoading = true,
                categoryLoading = true
            )
        )


}