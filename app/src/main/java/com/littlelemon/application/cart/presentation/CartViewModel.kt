package com.littlelemon.application.cart.presentation

import androidx.lifecycle.ViewModel
import com.littlelemon.application.cart.domain.usecase.ClearCartUseCase
import com.littlelemon.application.cart.domain.usecase.GetCartItemsUseCase
import com.littlelemon.application.cart.domain.usecase.UpsertCartItemUseCase

class CartViewModel(
    private val getCartItems: GetCartItemsUseCase,
    private val getCartError: GetCartItemsUseCase,
    private val upsertCartItem: UpsertCartItemUseCase,
    private val clearCart: ClearCartUseCase
) : ViewModel() {
}