package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class OptionSelectTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    private val options = listOf("Option 1", "Option 2", "Option 3")

    @Test
    fun optionSelect_displaysAllOptions() = runTest {
        // When an option list is passed to `OptionList`
        composeTestRule.setContent {
            OptionSelect(options, options[0], {})
        }

        // Then all options are displayed
        for (option in options) {
            composeTestRule.onNodeWithText(option).assertIsDisplayed()
        }
    }

    @Test
    fun optionSelect_onOptionClick_triggersCallback() = runTest {
        // When an `OptionList`
        var currentValue = options[0]
        composeTestRule.setContent {
            OptionSelect(options, options[0], { currentValue = it })
        }

        // When a second option is pressed
        composeTestRule.onNodeWithText(options[1]).performClick()

        // Then, the callback is triggered with correct value
        assertEquals(options[1], currentValue)

    }

}