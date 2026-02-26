package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
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

    private val application = RuntimeEnvironment.getApplication()
    private val overlayMatcher =
        hasTestTag(application.getString(R.string.test_tag_alert_dialog_overlay))

    @Test
    fun alertDialog_whenDisplayed_containsAllContent() {
        // Given the dialog is displayed
        composeTestRule.setContent {
            AlertDialog(
                dialogTitle = dialogTitle,
                dialogText = dialogContent,
                showDialog = true,
                positiveActionText = dialogPositiveAction,
                negativeActionText = dialogNegativeAction,
            ) {
            }
        }

        // Then, all the elements are displayed
        composeTestRule.onNodeWithText(dialogTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(dialogContent).assertIsDisplayed()
        composeTestRule.onNodeWithText(dialogPositiveAction).assertIsDisplayed()
        composeTestRule.onNodeWithText(dialogNegativeAction).assertIsDisplayed()
    }

    @Test
    fun alertDialogDismissable_whenDismissedByClickingOnBackground_onDismissDialogIsTriggered() {
        // Given a dismissable dialog is displayed
        var onDismissCallback = false
        composeTestRule.setContent {
            AlertDialog(
                dialogTitle = dialogTitle,
                dialogText = dialogContent,
                showDialog = true,
                dismissable = true,
                onDismissDialog = { onDismissCallback = true },
                positiveActionText = dialogPositiveAction,
                negativeActionText = dialogNegativeAction,
            ) {
            }
        }

        // When clicked on background
        composeTestRule.onNode(overlayMatcher).performClick()

        // Then, onDismiss is triggered
        assertTrue(onDismissCallback)
    }

    @Test
    fun alertDialogNonDismissable_whenDismissedByClickingOnBackground_onDismissDialogIsNotTriggered() {
        // Given a dismissable dialog is displayed
        var onDismissCallback = false
        composeTestRule.setContent {
            AlertDialog(
                dialogTitle = dialogTitle,
                dialogText = dialogContent,
                showDialog = true,
                dismissable = false,
                onDismissDialog = { onDismissCallback = true },
                positiveActionText = dialogPositiveAction,
                negativeActionText = dialogNegativeAction,
            ) {
            }
        }

        // When clicked on background
        composeTestRule.onNode(overlayMatcher).performClick()

        // Then, onDismiss is triggered
        assertFalse(onDismissCallback)
    }


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