package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class AlertDialogTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    private val dialogTitle = "Title"
    private val dialogContent = "Content"
    private val dialogPositiveAction = "PositiveAct"
    private val dialogNegativeAction = "NegativeAct"

    @Test
    fun alertDialog_whenDisplayedAndPositiveActionClicked_triggersPositiveActionCallback() {
        // Given the dialog is displayed
        var callbackTriggered = false
        composeTestRule.setContent {
            AlertDialog(
                dialogTitle = dialogTitle,
                dialogText = dialogContent,
                showDialog = true,
                positiveActionText = dialogPositiveAction,
                negativeActionText = dialogNegativeAction,
                onPositiveAction = { callbackTriggered = true }
            ) {
            }
        }

        // When the positive action button is clicked
        composeTestRule.onNodeWithText(dialogPositiveAction).performClick()

        // Then, button callback is not triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun alertDialog_whenDisplayedAndNegativeActionClicked_triggersNegativeActionCallback() {
        // Given the dialog is displayed
        var callbackTriggered = false
        composeTestRule.setContent {
            AlertDialog(
                dialogTitle = dialogTitle,
                dialogText = dialogContent,
                showDialog = true,
                positiveActionText = dialogPositiveAction,
                negativeActionText = dialogNegativeAction,
                onNegativeAction = { callbackTriggered = true }
            ) {
            }
        }

        // When the positive action button is clicked
        composeTestRule.onNodeWithText(dialogNegativeAction).performClick()

        // Then, button callback is not triggered
        assertTrue(callbackTriggered)
    }


    @Test
    fun alertDialog_whenDisplayed_doesNotAllowTouchEventsToPropagate() {
        // Given the dialog is displayed
        val buttonLabel = "Test Button"
        var onClicked = false
        composeTestRule.setContent {
            AlertDialog(
                dialogTitle = dialogTitle,
                dialogText = dialogContent,
                showDialog = true,
                positiveActionText = dialogPositiveAction,
                negativeActionText = dialogNegativeAction
            ) {
                Button(buttonLabel, onClick = { onClicked = true })
            }
        }

        // When the button is clicked
        composeTestRule.onNodeWithText(buttonLabel).performClick()

        // Then, button callback is not triggered
        assertFalse(onClicked)
    }

    @Test
    fun alertDialog_whenNotDisplayed_allowsTouchEventsToPropagate() {
        // Given the dialog is not displayed
        val buttonLabel = "Test Button"
        var onClicked = false
        composeTestRule.setContent {
            AlertDialog(
                dialogTitle = dialogTitle,
                dialogText = dialogContent,
                showDialog = false,
                positiveActionText = dialogPositiveAction,
                negativeActionText = dialogNegativeAction
            ) {
                Button(buttonLabel, onClick = { onClicked = true })
            }
        }

        // When the button is clicked
        composeTestRule.onNodeWithText(buttonLabel).performClick()

        // Then, button callback is not triggered
        assertTrue(onClicked)
    }


}