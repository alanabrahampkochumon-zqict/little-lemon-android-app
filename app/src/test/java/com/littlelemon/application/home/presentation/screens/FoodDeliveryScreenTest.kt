package com.littlelemon.application.home.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
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

    private val application = RuntimeEnvironment.getApplication()


    @Test
    fun categoryHeaderIsDisplayed() {
        testRule.setContent {
            LazyColumn {
                foodDeliveryContent(12.dp)
            }
        }

        testRule.onNodeWithText(application.getString(R.string.heading_order_for_delivery))
            .assertIsDisplayed()
    }

    @Test
    fun popularOrderHeaderIsDisplayed() {
        testRule.setContent {
            LazyColumn {
                foodDeliveryContent(12.dp)
            }
        }

        testRule.onNodeWithText(application.getString(R.string.heading_popular_orders))
            .assertIsDisplayed()
    }
}