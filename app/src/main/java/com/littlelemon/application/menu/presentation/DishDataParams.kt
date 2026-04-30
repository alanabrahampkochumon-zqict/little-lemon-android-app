package com.littlelemon.application.menu.presentation

import com.littlelemon.application.shared.menu.domain.util.DishFilter
import com.littlelemon.application.shared.menu.domain.util.DishSorting

/**
 * Data container used for passing and combining flows in @see MenuViewModel.
 */
internal data class DishDataParams(
    val sortingFlow: DishSorting,
    val filterFlow: DishFilter?,
    val forceFetchFlow: Boolean,
    val categoryFlow: String?
)
