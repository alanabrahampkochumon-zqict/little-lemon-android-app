package com.littlelemon.application.core.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.testing.junit.testparameterinjector.TestParameter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestParameterInjector
import kotlin.test.assertEquals

@RunWith(RobolectricTestParameterInjector::class)
class FilterListTest {


    @get:Rule
    val testRule = createComposeRule()

    companion object {
        private val filters = listOf("Filter 1", "Filter 2", "Filter 3", "Filter 4")
    }

    @Test
    fun displaysAllFilters(
        @TestParameter(
            "Filter 1", "Filter 2", "Filter 3", "Filter 4"
        ) filter: String
    ) {
        // Given a filter list
        testRule.setContent {
            FilterList(filters, filters[0], {})
        }

        // Then all the filters are displayed
        testRule.onNodeWithText(filter).assertIsDisplayed()
    }

    @Test
    fun correctlyDisplaySelectedState() {
        // Given a filter list with its first item selected
        testRule.setContent {
            FilterList(filters, filters[0], {})
        }

        // Then, the first item is selected
        testRule.onNodeWithText(filters[0]).assertIsSelected()

        // And others are not selected
        testRule.onNodeWithText(filters[1]).assertIsNotSelected()
        testRule.onNodeWithText(filters[2]).assertIsNotSelected()
        testRule.onNodeWithText(filters[3]).assertIsNotSelected()

    }

    @Test
    fun onSelectTriggersCallback() {
        // Given a filter list with its first item selected
        var callbackTriggeredWith = ""
        testRule.setContent {
            FilterList(filters, filters[0], { callbackTriggeredWith = it })
        }
        val selected = filters[2]

        // When a filter is selected
        testRule.onNodeWithText(selected).performClick()

        // Then, the item is selected
        assertEquals(selected, callbackTriggeredWith)

    }
}