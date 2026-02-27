package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LabelInputFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val label = "label"
    private val placeholder = "placeholder"
    private val value = "value"
    private val errorMessage = "errorMessage"

    @Test
    fun labelInputFiled_labelIsDisplayed() {
        // Given a label input field
        composeTestRule.setContent {
            LabelInputField(label = label, placeholder = placeholder, value = value)
        }

        // Then, label is displayed
        composeTestRule.onNodeWithText(label).assertIsDisplayed()
    }


    @Test
    fun inputField_nonEmptyValue_valueIsDisplayed() {
        // Given a label input field with value and placeholder
        composeTestRule.setContent {
            LabelInputField(label = label, placeholder = placeholder, value = value)
        }

        // Then, value is displayed
        composeTestRule.onNodeWithText(value).assertIsDisplayed()
    }


    @Test
    fun labelInputField_errorMessage_messageIsShown() {
        // Given a label input field with error message
        composeTestRule.setContent {
            LabelInputField(
                label = label,
                placeholder = placeholder,
                value = value,
                errorMessage = errorMessage
            )
        }

        // Then error message is shown
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}