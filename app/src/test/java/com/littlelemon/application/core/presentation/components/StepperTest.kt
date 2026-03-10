package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class StepperTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    private val increaseButtonMatcher by lazy {
        hasContentDescription(application.getString(R.string.desc_increase_quantity))
    }
    private val decreaseButtonMatcher by lazy {
        hasContentDescription(application.getString(R.string.desc_decrease_quantity))
    }

    @Test
    fun stepper_valueLessThanOne_incrementButtonIsDisplayed() = runTest {
        // Given a stepper with a value of 0
        composeTestRule.setContent {
            Stepper(0, {}, {})
        }

        // Then, the increment button is displayed
        composeTestRule.onNode(increaseButtonMatcher).assertIsDisplayed()
    }

    @Test
    fun stepper_valueLessThanOne_valueAndDecrementButtonNotDisplayed() = runTest {
        // Given a stepper with a value of 0
        composeTestRule.setContent {
            Stepper(0, {}, {})
        }

        // Then, the value and decrement button is not displayed
        composeTestRule.onNode(decreaseButtonMatcher).assertIsNotDisplayed()
        composeTestRule.onNodeWithText("0").assertIsNotDisplayed()
    }

    @Test
    fun stepper_valueEqualToOne_valueAndDecrementButtonIsDisplayed() = runTest {
        // Given a stepper with a value of 1
        composeTestRule.setContent {
            Stepper(1, {}, {})
        }

        // Then, the value and decrement button is displayed
        composeTestRule.onNode(decreaseButtonMatcher).assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun stepper_valueGreaterThanOne_valueAndDecrementButtonIsDisplayed() = runTest {
        // Given a stepper with a value of 5
        composeTestRule.setContent {
            Stepper(5, {}, {})
        }

        // Then, the value and decrement button is displayed
        composeTestRule.onNode(decreaseButtonMatcher).assertIsDisplayed()
        composeTestRule.onNodeWithText("5").assertIsDisplayed()
    }

    @Test
    fun stepper_decrementButtonPressed_triggersCallback() = runTest {
        // Given a stepper with a value of 5
        var callbackTriggered = false
        composeTestRule.setContent {
            Stepper(5, { callbackTriggered = true }, {})
        }

        // When the decrement button is pressed
        composeTestRule.onNode(decreaseButtonMatcher).performClick()

        // Then, the callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun stepper_incrementButtonPressed_triggersCallback() = runTest {
        // Given a stepper with a value of 5
        var callbackTriggered = false
        composeTestRule.setContent {
            Stepper(5, { }, { callbackTriggered = true })
        }

        // When the increment button is pressed
        composeTestRule.onNode(increaseButtonMatcher).performClick()

        // Then, the callback is triggered
        assertTrue(callbackTriggered)
    }

}