package com.littlelemon.application.auth.presentation.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
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
import kotlin.test.assertTrue

@MediumTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class VerificationContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    private val verifyButtonText by lazy { application.getString(R.string.act_verify) }

    private val verifyButtonMatcher by lazy {
        hasText(verifyButtonText) and hasClickAction()
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

    @Test
    fun verificationContent_onOTPChange_otpChangeCallbackIsTriggered() {
        // Given OTP Screen with empty fields
        var callbackTriggered = false

        // NOTE otp is received in the fashion of [1, null, null..], [null, 2, ...], etc

        composeTestRule.setContent {
            VerificationContent(
                AuthState(),
                onOTPChange = { callbackTriggered = true })
        }

        // When an otp is entered
        val otpFields = composeTestRule.onAllNodes(otpInputMatcher)
        repeat(6) {
            otpFields[it].performTextInput(it.toString())
        }

        // Then the otp change callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun verificationContent_onVerify_verifyCallbackIsTriggered() {
        // Given OTP Screen with verify button enabled
        var callbackTriggered = false
        composeTestRule.setContent {
            VerificationContent(
                AuthState(enableVerifyButton = true),
                onVerifyOTP = { callbackTriggered = true })
        }

        // When verify button is pressed
        composeTestRule.onNode(verifyButtonMatcher).performClick()

        // Then callback is triggered
        assertTrue(callbackTriggered)
    }
}