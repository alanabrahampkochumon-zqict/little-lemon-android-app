package com.littlelemon.application.auth.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.screens.LoginContent
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    val buttonText by lazy { composeTestRule.activity.getString(R.string.act_send_otp) }
    val emailPlaceholder by lazy { composeTestRule.activity.getString(R.string.placeholder_email_address) }
//    val emailFieldTag = composeTestRule.activity.getString(R.string.test_tag_email_field)

    val emailError = "Invalid email address"
    val validEmail = "test@littlemon.com"
    val invalidEmail = "test.com"

    @Test
    fun loginScreen_noEmail_sendOTPButtonDisabledAndPlaceholderIsShown() {
        // Given empty email (state)
        val state = AuthState()

        composeTestRule.setContent {
            Column {
                LoginContent(state)
            }
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
            Column {
                LoginContent(state)
            }
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
            Column {
                LoginContent(state)
            }
        }

        // Then: Send OTP button is enabled, email is displayed and placeholder is hidden
        composeTestRule.onNodeWithText(emailPlaceholder).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(invalidEmail).assertIsDisplayed()
        composeTestRule.onNodeWithText(buttonText).assertIsNotEnabled()
        composeTestRule.onNodeWithText(emailError).assertIsDisplayed()

    }
}