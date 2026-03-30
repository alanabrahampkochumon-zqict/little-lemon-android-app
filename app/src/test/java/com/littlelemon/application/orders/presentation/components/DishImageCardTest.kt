package com.littlelemon.application.orders.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class DishImageCardTest {

    @get:Rule
    val testRule = createComposeRule()

    private val quantity = 5
    private val dishName = "New Dish"

    @Test
    fun displaysQuantityLessThan10() {
        testRule.setContent {
            DishImageCard("url", dishName, quantity)
        }

        testRule.onNodeWithText("$quantity").assertIsDisplayed()
    }

    @Test
    fun `displays quantity greater than 9 with 9+`() {
        testRule.setContent {
            DishImageCard("url", dishName, 15)
        }
        testRule.onNodeWithText("9+").assertIsDisplayed()
    }

    @Test
    fun displaysDishName() {
        testRule.setContent {
            DishImageCard("url", dishName, quantity)
        }

        testRule.onNodeWithText(dishName).assertIsDisplayed()
    }

}