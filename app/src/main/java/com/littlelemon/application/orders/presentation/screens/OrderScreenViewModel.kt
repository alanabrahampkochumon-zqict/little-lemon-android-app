package com.littlelemon.application.orders.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.shared.cart.domain.usecase.GetCartItemDetailsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class OrderScreenViewModel(
    getCartItems: GetCartItemDetailsUseCase,
    getAddress: GetAddressUseCase,
) : ViewModel() {


    val state = combine(getCartItems(), getAddress()) { cartItems, addressResource ->
        var state = OrderScreenState(
            isCartItemLoading = false,
            cartItemError = null,
            cartItems = cartItems,
            isAddressLoading = true
        )

        state = when (addressResource) {
            is Resource.Failure -> state.copy(
                isAddressLoading = false,
                addressError = UiText.StringResource(R.string.address_loading_error_message)
            )

            is Resource.Loading -> state
            is Resource.Success -> state.copy(
                addressError = null,
                isAddressLoading = false,
                defaultAddress = addressResource.data?.first { it.isDefault })
        }

        state
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        OrderScreenState(isCartItemLoading = true, isAddressLoading = true)
    )
}