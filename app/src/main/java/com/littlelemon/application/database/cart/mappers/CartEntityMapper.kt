package com.littlelemon.application.database.cart.mappers

import com.littlelemon.application.database.cart.models.CartItemDetails
import com.littlelemon.application.shared.cart.domain.models.CartDetailItem
import com.littlelemon.application.shared.menu.data.mappers.toDish


fun List<CartItemDetails>.toCartItems(): List<CartDetailItem> {
    return map { (cartItem, dishEntity) ->
        CartDetailItem(dishEntity.toDish(), cartItem.quantity)
    }
}

//fun List<CartItemEntity>.toCartItems(): List<CartItem> {}