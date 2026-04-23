package com.littlelemon.application.core.presentation.components

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
class PriceRowTest {

    @get:Rule
    val testRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    private val positivePrice = 55.0
    private val expectedPositivePrice = "$55.00"
    private val negativePrice = -55.0
    private val expectedNegativePrice = "-$55.00"
    private val free = application.getString(R.string.free)
    private val label = "pricing section"

    @Test
    fun displaysLabel() {
        testRule.setContent {
            PriceRow(label, positivePrice)
        }
        testRule.onNodeWithText(label).assertIsDisplayed()
    }

    @Test
    fun positivePrice_displaysFormattedPrice() {
        testRule.setContent {
            PriceRow(label, positivePrice)
        }
        testRule.onNodeWithText(expectedPositivePrice).assertIsDisplayed()
    }

    @Test
    fun negativePrice_displaysFormattedPriceWithSign() {
        testRule.setContent {
            PriceRow(label, negativePrice)
        }
        testRule.onNodeWithText(expectedNegativePrice).assertIsDisplayed()
    }

    @Test
    fun zero_displaysFree() {
        testRule.setContent {
            PriceRow(label, 0.0)
        }
        testRule.onNodeWithText(free).assertIsDisplayed()
    }
}