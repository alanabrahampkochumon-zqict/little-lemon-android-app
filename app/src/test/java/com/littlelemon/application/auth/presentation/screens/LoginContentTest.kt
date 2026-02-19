package com.littlelemon.application.auth.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.MediumTest
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.AuthState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@MediumTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LoginContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    val buttonText by lazy { application.getString(R.string.act_send_otp) }
    val emailPlaceholder by lazy { application.getString(R.string.placeholder_email_address) }

    val emailError = "Invalid email address"
    val validEmail = "test@littlemon.com"
    val invalidEmail = "test.com"

    @Test
    fun loginScreen_noEmail_sendOTPButtonDisabledAndPlaceholderIsShown() {
        // Given empty email (state)
        val state = AuthState()

        composeTestRule.setContent {
            LoginContent(state)
        }

        // Then: Email Placeholder is shown and  `Send OTP` Button is shown
        composeTestRule.onNodeWithText(emailPlaceholder).assertIsDisplayed()
        composeTestRule.onNodeWithText(buttonText).assertIsNotEnabled()
    }

    @Test
    fun loginScreen_validEmail_sendOTPButtonIsEnabledAndEmailIsShown() {
        // Given valid email (state)
        val state = AuthState(email = validEmail, enableSendButton = true)

        composeTestRule.setContent {
            LoginContent(state)
        }

        // Then: Send OTP button is enabled, email is displayed and placeholder is hidden
        composeTestRule.onNodeWithText(emailPlaceholder).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(validEmail).assertIsDisplayed()
        composeTestRule.onNodeWithText(buttonText).assertIsEnabled()
    }

    @Test
    fun loginScreen_invalidEmail_errorTextIsShownAndSendOTPButtonIsDisabled() {
        // Given valid email (state)
        val state = AuthState(email = invalidEmail, emailError = emailError)

        composeTestRule.setContent {
            LoginContent(state)
        }

        // Then: Send OTP button is enabled, email is displayed and placeholder is hidden
        composeTestRule.onNodeWithText(emailPlaceholder).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(invalidEmail).assertIsDisplayed()
        composeTestRule.onNodeWithText(buttonText).assertIsNotEnabled()
        composeTestRule.onNodeWithText(emailError).assertIsDisplayed()
    }

    @Test
    fun loginScreen_onEmailChange_emailChangeCallbackIsTriggered() {

        var newEmail = ""

        // Given login screen
        composeTestRule.setContent {
            LoginContent(AuthState(), onEmailChange = { newEmail = it })
        }

        // When a valid email is input into text field
        composeTestRule.onNodeWithText(emailPlaceholder).performTextInput(validEmail)

        // Then email change callback is triggered
        assertEquals(validEmail, newEmail)
    }

    @Test
    fun loginScreen_onSendOTPButtonPress_callbackIsTriggered() {
        var callbackTriggered = false

        // Given login screen with send otp button enabled
        composeTestRule.setContent {
            LoginContent(
                AuthState(enableSendButton = true),
                onSendOTP = { callbackTriggered = true })
        }

        // When send OTP button is pressed
        composeTestRule.onNodeWithText(buttonText).performClick()

        // Then sendOTP callback is triggered
        assertTrue { callbackTriggered }
    }
}