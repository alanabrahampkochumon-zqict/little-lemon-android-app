package com.littlelemon.application.core.presentation.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
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
    fun optionSelect_firsOptionIsSelectedByDefault() = runTest {
        // When an option list is passed to `OptionList`
        composeTestRule.setContent {
            OptionSelect(options, options[0], {})
        }

        // Then, first option is selected
        composeTestRule.onNodeWithText(options[0]).assertIsSelected()
    }

    @Test
    fun optionSelected_onOptionClick_deselectCurrentItemAndSelectNewItem() = runTest {
        // When an `OptionList`
        var selectedOption by mutableStateOf(options[0])
        composeTestRule.setContent {
            OptionSelect(options, selectedOption, { selectedOption = it })
        }

        // When a second option is pressed
        composeTestRule.onNodeWithText(options[1]).performClick()

        // Then, first option is deselected
        composeTestRule.onNodeWithText(options[0]).assertIsNotSelected()
        // and, second option is selected
        composeTestRule.onNodeWithText(options[1]).assertIsSelected()
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