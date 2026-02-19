package com.littlelemon.application.auth.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.littlelemon.application.R
import com.littlelemon.application.auth.presentation.screens.VerificationContent
import org.junit.Rule
import org.junit.Test

class VerificationContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val verifyButtonMatcher by lazy {
        hasText(composeTestRule.activity.getString(R.string.act_verify)) and hasClickAction()
    }
    private val otpInputMatcher by lazy { hasSetTextAction() }

    private val numOTP = 6

    @Test
    fun verificationContent_emptyOTP_OTPFieldsAreEmpty() {
        // Given a state with empty OTP fields
        val state = AuthState(oneTimePassword = List(numOTP) { null })

        composeTestRule.setContent {
            VerificationContent(state)
        }

        // Then check all 6 otp fields are empty
        val otpFields = composeTestRule.onAllNodes(otpInputMatcher)
        otpFields.assertCountEquals(numOTP)
        repeat(numOTP) { index ->
            otpFields[index].assertTextEquals("")
        }
    }

    @Test
    fun verificationContent_completeOTP_otpFieldsShowCorrectOTP() {
        // Given a state with fully filled OTP fields
        val state = AuthState(oneTimePassword = List(numOTP) { it })

        composeTestRule.setContent {
            VerificationContent(state)
        }

        // Then check all 6 otp fields are displaying index values(0..5)
        val otpFields = composeTestRule.onAllNodes(otpInputMatcher)
        otpFields.assertCountEquals(numOTP)
        repeat(numOTP) { index ->
            otpFields[index].assertTextEquals("$index")
        }
    }

    @Test
    fun verificationContent_incompleteOTP_verifyButtonIsDisabled() {
        // Given a state with partially filled OTP
        val state = AuthState(
            oneTimePassword = listOf(1, 2, 3, null, null, null),
            enableVerifyButton = false
        )

        composeTestRule.setContent {
            VerificationContent(state)
        }

        // Then check all 6 otp fields are displaying index values(0..5)
        composeTestRule.onNode(verifyButtonMatcher).assertIsNotEnabled()
    }

    @Test
    fun verificationContent_completeOTP_verifyButtonIsEnabled() {
        // Given a state with fully filled OTP
        val state = AuthState(oneTimePassword = List(numOTP) { it }, enableVerifyButton = true)

        composeTestRule.setContent {
            VerificationContent(state)
        }

        // Then verify button is enabled
        composeTestRule.onNode(verifyButtonMatcher).assertIsEnabled()
    }
}