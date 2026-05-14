package com.littlelemon.application.orders.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlelemon.application.orders.presentation.OrderScreenState
import com.littlelemon.application.shared.cart.domain.usecase.GetCartItemDetailsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class OrderScreenViewModel(
    getCartItems: GetCartItemDetailsUseCase,
) : ViewModel() {


    val state = combine(getCartItems()) { (cartItems) ->
        OrderScreenState(isCartItemLoading = false, cartItemError = null, cartItems = cartItems)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        OrderScreenState(isCartItemLoading = true)
    )
}