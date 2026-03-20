package com.littlelemon.application.core.presentation.components


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.core.CoreTestTags
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class BasicStepperTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun stepper_valueLessThanOne_incrementButtonIsDisplayed() = runTest {
        // Given a stepper with a value of 0
        composeTestRule.setContent {
            BasicStepper(0, {}, {})
        }

        // Then, the increment button is displayed
        composeTestRule.onNodeWithTag(CoreTestTags.STEPPER_INCREASE).assertIsDisplayed()
    }

    @Test
    fun stepper_valueLessThanOne_valueAndDecrementButtonNotDisplayed() = runTest {
        // Given a stepper with a value of 0
        composeTestRule.setContent {
            BasicStepper(0, {}, {})
        }

        // Then, the value and decrement button is not displayed
        composeTestRule.onNodeWithTag(CoreTestTags.STEPPER_DECREASE).assertIsNotDisplayed()
        composeTestRule.onNodeWithText("0").assertIsNotDisplayed()
    }

    @Test
    fun stepper_valueEqualToOne_valueAndDecrementButtonIsDisplayed() = runTest {
        // Given a stepper with a value of 1
        composeTestRule.setContent {
            BasicStepper(1, {}, {})
        }

        // Then, the value and decrement button is displayed
        composeTestRule.onNodeWithTag(CoreTestTags.STEPPER_DECREASE).assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun stepper_valueGreaterThanOne_valueAndDecrementButtonIsDisplayed() = runTest {
        // Given a stepper with a value of 5
        composeTestRule.setContent {
            BasicStepper(5, {}, {})
        }

        // Then, the value and decrement button is displayed
        composeTestRule.onNodeWithTag(CoreTestTags.STEPPER_DECREASE).assertIsDisplayed()
        composeTestRule.onNodeWithText("5").assertIsDisplayed()
    }

    @Test
    fun stepper_decrementButtonPressed_triggersCallback() = runTest {
        // Given a stepper with a value of 5
        var callbackTriggered = false
        composeTestRule.setContent {
            BasicStepper(5, { callbackTriggered = true }, {})
        }

        // When the decrement button is pressed
        composeTestRule.onNodeWithTag(CoreTestTags.STEPPER_DECREASE).performClick()

        // Then, the callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun stepper_incrementButtonPressed_triggersCallback() = runTest {
        // Given a stepper with a value of 5
        var callbackTriggered = false
        composeTestRule.setContent {
            BasicStepper(5, { }, { callbackTriggered = true })
        }

        // When the increment button is pressed
        composeTestRule.onNodeWithTag(CoreTestTags.STEPPER_INCREASE).performClick()

        // Then, the callback is triggered
        assertTrue(callbackTriggered)
    }

}