package com.littlelemon.application.orders.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import com.littlelemon.application.orders.domain.models.MenuItem
import kotlinx.datetime.LocalDateTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OrderItemTest {

    @get:Rule
    val testRule = createComposeRule()

    private val menuItem = MenuItem(
        Dish(
            title = "Dish Name",
            description = "Dish Description",
            price = 100.00,
            imageURL = "testurl",
            stock = 100,
            nutritionInfo = NutritionInfo(100, 1, 1, 1),
            discountedPrice = 50.00,
            category = listOf(),
            dateAdded = LocalDateTime(2026, 12, 12, 12, 12, 12),
            popularityIndex = 100
        ), 5
    )

    @Test
    fun dishTitle_isDisplayed() {
        // When an order item is rendered
        testRule.setContent {
            OrderItem(menuItem, {}, {}, {})
        }

        // Then, title is displayed
        testRule.onNodeWithText(menuItem.dish.title).assertIsDisplayed()
    }

}