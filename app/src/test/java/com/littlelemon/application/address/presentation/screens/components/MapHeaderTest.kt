package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.littlelemon.application.address.presentation.AddressTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class MapHeaderTest {


    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun closeButton_whenClicked_triggersCallback() {
        // Given a map header
        var callbackTriggered = false
        testRule.setContent {
            MapHeader(
                floatingBarTopPadding = 0.dp,
                floatingBarBottomPadding = 0.dp,
                onFetchCurrentLocation = {},
                onClose = { callbackTriggered = true })
        }

        // When close button is pressed
        testRule.onNodeWithTag(AddressTestTags.HEADER_CLOSE_BUTTON).performClick()

        // Then callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun fetchLocationButton_whenClicked_triggersCallback() {
        // Given a map header
        var callbackTriggered = false
        testRule.setContent {
            MapHeader(
                floatingBarTopPadding = 0.dp,
                floatingBarBottomPadding = 0.dp,
                onFetchCurrentLocation = { callbackTriggered = true },
                onClose = {})
        }

        // When close button is pressed
        testRule.onNodeWithTag(AddressTestTags.MAP_HEADER_FETCH_LOCATION_BUTTON)
            .performClick()

        // Then callback is triggered
        assertTrue(callbackTriggered)
    }
}