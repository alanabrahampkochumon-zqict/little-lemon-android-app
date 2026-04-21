package com.littlelemon.application.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.littlelemon.application.R

enum class NavigationOption(
    @field:DrawableRes val selectedIcon: Int,
    @field:DrawableRes val defaultIcon: Int,
    @field:StringRes val label: Int,
) {
    HOME(R.drawable.ic_home_filled, R.drawable.ic_home_outline, R.string.navigation_home),
    MENU(R.drawable.ic_hat_filled, R.drawable.ic_hat_outline, R.string.navigation_menu),
    ORDER(R.drawable.ic_bill_filled, R.drawable.ic_bill_outline, R.string.navigation_order),
    CART(R.drawable.ic_bag_filled, R.drawable.ic_bag_outline, R.string.navigation_cart),
    PROFILE(
        R.drawable.ic_profile_filled,
        R.drawable.ic_profile_outline,
        R.string.navigation_profile
    )
}