package com.littlelemon.application.home.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.littlelemon.application.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class FoodDeliveryScreenTest {

    @get:Rule
    val testRule = createComposeRule()

    val application = RuntimeEnvironment.getApplication()


    @Test
    fun categoryHeaderIsDisplayed() {
        // When Food Delivery Screen is rendered
        testRule.setContent {
            FoodDeliveryScreen()
        }

        // Then, order for delivery is displayed
        testRule.onNodeWithText(application.getString(R.string.heading_order_for_delivery))
            .assertIsDisplayed()
    }
}