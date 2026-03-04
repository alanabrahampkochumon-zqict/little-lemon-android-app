package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.filters.MediumTest
import com.littlelemon.application.core.CoreTestTags
import junit.framework.TestCase.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@MediumTest
@RunWith(RobolectricTestRunner::class)
class BottomSheetTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var bottomSheetBackground: SemanticsNodeInteraction
    private lateinit var bottomSheet: SemanticsNodeInteraction
    
    @Before
    fun setUp() {
        composeTestRule.mainClock.autoAdvance = false
        bottomSheet = composeTestRule.onNodeWithTag(CoreTestTags.TEST_TAG_BOTTOM_SHEET)
        bottomSheetBackground =
            composeTestRule.onNodeWithTag(CoreTestTags.TEST_TAG_BOTTOM_SHEET_BACKGROUND)

    }


    @Test
    fun bottomSheet_whenVisible_showsBottomSheet() {
        // When a bottom sheet is visible
        composeTestRule.setContent {
            BottomSheet(showBottomSheet = true)
        }

        composeTestRule.mainClock.advanceTimeBy(2000L)

        // Then, the overlay and modal is visible
        bottomSheetBackground.assertIsDisplayed()
        bottomSheet.assertIsDisplayed()

    }

    @Test
    fun bottomSheet_whenClickedOnBackground_triggeredOnDismissCallback() {
        // Given a bottom sheet is visible
        var callbackTriggered = false
        composeTestRule.setContent {
            BottomSheet(
                showBottomSheet = true,
                onDismiss = { callbackTriggered = true },
                dismissable = true
            )
        }

        composeTestRule.mainClock.advanceTimeBy(2000L)

        // When the background is clicked
        bottomSheetBackground.performSemanticsAction(SemanticsActions.OnClick)

        // Then, the callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun bottomSheet_nonDismissible_whenClickedOnBackground_doesNotTriggerCallback() {
        // Given a bottom sheet is visible
        var callbackTriggered = false
        composeTestRule.setContent {
            BottomSheet(
                showBottomSheet = true,
                onDismiss = { callbackTriggered = true },
                dismissable = false
            )
        }

        composeTestRule.mainClock.advanceTimeBy(2000L)

        // When the background is clicked
        bottomSheetBackground.performClick()

        // Then, the callback is not triggered
        assertFalse(callbackTriggered)
        // and bottom sheet is visible
        bottomSheet.assertIsDisplayed()
    }

    @Test
    fun bottomSheet_onSheetClick_doesNotCloseBottomSheet() {
        // Given a bottom sheet is visible
        var callbackTriggered = false
        composeTestRule.setContent {
            BottomSheet(
                showBottomSheet = true,
                onDismiss = { callbackTriggered = true },
                dismissable = false
            )
        }

        composeTestRule.mainClock.advanceTimeBy(2000L)

        // When the bottomSheet is clicked
        bottomSheet.performClick()

        // Then, the callback is not triggered
        assertFalse(callbackTriggered)
        // and bottom sheet is visible
        bottomSheet.assertIsDisplayed()
    }

    @Test
    fun bottomSheet_passedInContent_rendersCorrectly() {
        // When a bottom sheet has some content
        val buttonText = "Test button"
        var callbackTriggered = false
        composeTestRule.setContent {
            BottomSheet(
                showBottomSheet = true,
                onDismiss = { callbackTriggered = true },
                dismissable = false
            ) {
                Button(buttonText, onClick = {})
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000L)

        // Then the content is rendered
        composeTestRule.onNodeWithText(buttonText).assertIsDisplayed()
    }

    @Test
    fun bottomSheet_onSheetContentClick_correctlyPropagatesCallbacks() {
        // When a bottom sheet has some content
        val buttonText = "Test button"
        var callbackTriggered = false
        composeTestRule.setContent {
            BottomSheet(
                showBottomSheet = true,
                dismissable = false
            ) {
                Button(buttonText, onClick = { callbackTriggered = true })
            }
        }

        composeTestRule.mainClock.advanceTimeBy(2000L)

        // When the button is clicked
        composeTestRule.onNodeWithText(buttonText).performClick()

        // Then, the callback is triggered
        assertTrue(callbackTriggered)
    }
}