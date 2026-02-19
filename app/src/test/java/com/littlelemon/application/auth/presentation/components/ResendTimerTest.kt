package com.littlelemon.application.auth.presentation.components

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import com.littlelemon.application.R
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
class ResendTimerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val application: Application = RuntimeEnvironment.getApplication()
    val resendButtonText = application.getString(R.string.act_resend_otp)

    @Test
    fun resendTimer_timerRunning_timerIsShown() {
        // Given a timer with valid time(>0)
        composeTestRule.setContent {
            ResendTimer(
                initialTime = 60
            )
        }

        // Verify that 60 is visible
        composeTestRule.onNodeWithText("60").assertIsDisplayed()

        // And resend button is not shown
        composeTestRule.onNodeWithText("Resend code").assertDoesNotExist()
    }

    @Test
    fun resendTimer_timerRunning_timeAdvances() {
        // Given a timer with valid time(>0)
        composeTestRule.setContent {
            ResendTimer(initialTime = 59)
        }

        // When the time advances by 10 seconds
        composeTestRule.mainClock.advanceTimeBy(10_000)

        // Then the resend timer shows 51 second
        composeTestRule.onNodeWithText("50").assertIsDisplayed()
    }

    @Test
    fun resendTimer_timerExpired_resendButtonIsShown() {
        // Given a timer with invalid time(0)
        var buttonClicked = false
        composeTestRule.setContent {
            ResendTimer(
                initialTime = 0,
                onResendCode = { buttonClicked = true }
            )
        }

        // Verify resend button is shown
        val button = composeTestRule.onNode(hasText(resendButtonText))
        button.assertIsDisplayed()

        // and button clicks gives calls callback function
        button.performClick()
        assertTrue { buttonClicked }
    }
}