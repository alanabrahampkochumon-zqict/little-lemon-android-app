package com.littlelemon.application.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import com.littlelemon.application.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class TextInputFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val placeholder = "placeholder"
    private val value = "value"
    private val errorMessage = "errorMessage"

    @DrawableRes
    private val leftIcon = R.drawable.ic_launcher_foreground
    private val leftIconDescription = "left icon"

    @DrawableRes
    private val rightIcon = R.drawable.ic_launcher_background
    private val rightIconDescription = "right icon"


    @Test
    fun inputField_placeholderAndEmptyValue_placeholderIsDisplayed() {
        // Given an input field with empty value and a placeholder
        composeTestRule.setContent {
            TextInputField(placeholder = placeholder, value = "")
        }

        // Then, placeholder is displayed
        composeTestRule.onNodeWithText(placeholder).assertIsDisplayed()
    }

    @Test
    fun inputField_placeholderAndNonEmptyValue_placeholderIsHiddenAndValueIsDisplayed() {
        // Given an input field with value and placeholder
        composeTestRule.setContent {
            TextInputField(placeholder = placeholder, value = value)
        }

        // Then, placeholder is not displayed and value is displayed
        composeTestRule.onNodeWithText(placeholder).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(value).assertIsDisplayed()
    }

    @Test
    fun inputField_disabled_isNotEnabled() {
        // Given a disabled input field
        composeTestRule.setContent {
            TextInputField(
                placeholder = placeholder,
                value = value,
                enabled = false
            )
        }

        // Then, the input field is not enabled.
        composeTestRule.onNodeWithText(value).assertIsNotEnabled()
    }

    @Test
    fun inputField_enabled_allowInput() {
        // Given an enabled input field
        var newValue = ""
        val changedValue = "changed value"
        composeTestRule.setContent {
            TextInputField(
                placeholder = placeholder,
                value = "value",
                onValueChange = { newValue = it },
                enabled = true
            )
        }

        // When input is typed in
        composeTestRule.onNodeWithText(value).performTextInput(changedValue)

        // Then the input value has changed
        assertEquals(changedValue + value, newValue) // + value since changedValue is prepended
    }

    @Test
    fun inputField_errorMessage_messageIsDisplayed() {
        // Given an input field with error message
        composeTestRule.setContent {
            TextInputField(
                placeholder = placeholder,
                value = value,
                errorMessage = errorMessage
            )
        }

        // Then error message is displayed
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun inputField_leftIconWithDescriptionPassedIn_leftIconDescriptionExists() {
        // Given an input field with left icon
        composeTestRule.setContent {
            TextInputField(
                placeholder = placeholder,
                value = value,
                iconLeft = leftIcon,
                iconLeftDescription = leftIconDescription
            )
        }

        // Then component with left icon description is displayed
        composeTestRule.onNodeWithContentDescription(leftIconDescription).assertIsDisplayed()
    }

    @Test
    fun inputField_rightIconWithDescriptionPassedIn_rightIconDescriptionExists() {
        // Given an input field with right icon
        composeTestRule.setContent {
            TextInputField(
                placeholder = placeholder,
                value = value,
                iconRight = rightIcon,
                iconRightDescription = rightIconDescription
            )
        }

        // Then component with right icon description is displayed
        composeTestRule.onNodeWithContentDescription(rightIconDescription).assertIsDisplayed()
    }

    @Test
    fun inputField_onIMEAction_triggersCorrectCallback() {
        // Given an ime action is triggered
        var callbackTriggered = false
        composeTestRule.setContent {
            TextInputField(
                placeholder = placeholder,
                value = value,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { callbackTriggered = true })
            )
        }

        // When ime action is pressed
        composeTestRule.onNodeWithText(value).performImeAction()

        // Then, the callback is triggered
        assertTrue(callbackTriggered)
    }
}