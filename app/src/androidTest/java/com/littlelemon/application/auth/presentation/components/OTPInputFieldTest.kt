package com.littlelemon.application.auth.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OTPInputFieldTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    val otpFieldTag = "OTP_FIELD"

    @Test
    fun otpInput_whenEmptyAndDigitEntered_callsOnNumberChanged() {
        // Given an empty otp field
        var capturedNumber: Int? = -1

        composeTestRule.setContent {
            OTPInputField(
                focusRequester = FocusRequester(),
                number = null,
                onNumberChanged = { capturedNumber = it },
                onFocusChanged = {},
                onKeyboardBack = {},
                modifier = Modifier.testTag(otpFieldTag)
            )
        }

        // When clicked and OTP a number is entered
        composeTestRule.onNodeWithTag(otpFieldTag).performClick()
        composeTestRule.onNode(hasSetTextAction()).performTextInput("5")

        // Assert that the callback received is 5
        assertEquals(5, capturedNumber)
    }

    @Test
    fun otpInput_whenNotEmptyAndDigitEntered_replacesExistingNumber() {
        // Given an non-empty otp field
        var capturedNumber: Int? = 5

        composeTestRule.setContent {
            OTPInputField(
                focusRequester = FocusRequester(),
                number = capturedNumber,
                onNumberChanged = { capturedNumber = it },
                onFocusChanged = {},
                onKeyboardBack = {},
                modifier = Modifier.testTag(otpFieldTag)
            )
        }

        // When clicked and a number is entered
        composeTestRule.onNodeWithTag(otpFieldTag).performClick()
        composeTestRule.onNode(hasSetTextAction()).performTextInput("7")

        // Assert that the field is updated(callback called)
        assertEquals(7, capturedNumber)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun otpInput_whenFilledAndBackspacePressed_deletesExistingNumber() {
        // Given an non-empty otp field
        var capturedNumber: Int? = 5

        composeTestRule.setContent {
            OTPInputField(
                focusRequester = FocusRequester(),
                number = capturedNumber,
                onNumberChanged = { capturedNumber = it },
                onFocusChanged = {},
                onKeyboardBack = {},
                modifier = Modifier.testTag(otpFieldTag)
            )
        }

        // When clicked and a backspace is entered
        composeTestRule.onNodeWithTag(otpFieldTag).performClick()
        composeTestRule.onNode(hasSetTextAction())
            .performKeyInput { pressKey(Key.Backspace) }

        // Assert that the captured field is non-empty
        assertNull(capturedNumber)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun otpInput_whenEmptyAndBackspacePressed_onKeyboardBackIsCalled() {
        // Given an empty otp field
        var onKeyboardBack = false

        composeTestRule.setContent {
            OTPInputField(
                focusRequester = FocusRequester(),
                number = null,
                onNumberChanged = { },
                onFocusChanged = {},
                onKeyboardBack = { onKeyboardBack = true },
                modifier = Modifier.testTag(otpFieldTag)
            )
        }

        // When clicked and a backspace is entered
        composeTestRule.onNodeWithTag(otpFieldTag).performClick()
        composeTestRule.onNodeWithTag(otpFieldTag).performKeyInput { pressKey(Key.Backspace) }

        // Assert onKeyboardBack is called
        assertTrue { onKeyboardBack }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun otpInput_whenNonDigitIsEntered_keyIsIgnored() {
        // Given an empty otp field
        var capturedNumber: Int? = null

        composeTestRule.setContent {
            OTPInputField(
                focusRequester = FocusRequester(),
                number = capturedNumber,
                onNumberChanged = { capturedNumber = it },
                onFocusChanged = {},
                onKeyboardBack = {},
                modifier = Modifier.testTag(otpFieldTag)
            )
        }

        // When clicked and a non-digit is entered
        composeTestRule.onNodeWithTag(otpFieldTag).performClick()
        composeTestRule.onNode(hasSetTextAction()).performTextInput("a")

        // Assert that the field is not updated(null)
        assertNull(capturedNumber)
    }
}