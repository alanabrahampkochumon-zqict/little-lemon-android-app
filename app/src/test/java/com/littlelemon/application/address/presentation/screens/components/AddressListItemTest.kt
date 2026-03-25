package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.address.presentation.AddressTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class AddressListItemTest {

    @get:Rule
    val testRule = createComposeRule()

    private val addressLabel = "Address Label"
    private val address = "Address, Address"

    @Test
    fun displayAddressLabel() {
        // Given an address label
        testRule.setContent {
            AddressListItem(addressLabel, address, false, {})
        }

        // Then, address label is displayed
        testRule.onNodeWithText(addressLabel).assertIsDisplayed()
    }

    @Test
    fun displaysAddress() {
        // Given an address label
        testRule.setContent {
            AddressListItem(addressLabel, address, false, {})
        }

        // Then, address is displayed
        testRule.onNodeWithText(address).assertIsDisplayed()
    }

    @Test
    fun onSelected_displaysCheckmark() {
        // Given a selected address label
        testRule.setContent {
            AddressListItem(addressLabel, address, true, {})
        }

        // Then, checkmark is displayed
        testRule.onNodeWithTag(AddressTestTags.ADDRESS_ITEM_CHECK_ICON, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun onSelected_semanticallySelected() {
        // Given a selected address label
        val tag = "AddressListTag"
        testRule.setContent {
            AddressListItem(
                addressLabel,
                address,
                true,
                {},
                modifier = Modifier.Companion.testTag(tag)
            )
        }

        // Then, address list item is selected
        testRule.onNodeWithTag(tag).assertIsSelected()
    }

    @Test
    fun onUnselected_semanticallyUnselected() {
        // Given an unselected address label
        val tag = "AddressListTag"
        testRule.setContent {
            AddressListItem(
                addressLabel,
                address,
                false,
                {},
                modifier = Modifier.Companion.testTag(tag)
            )
        }

        // Then, address list item is unselected
        testRule.onNodeWithTag(tag).assertIsNotSelected()
    }

    @Test
    fun onClick_triggersCallback() {
        // Given a selected address label
        val tag = "AddressListTag"
        var callbackTriggered = false
        testRule.setContent {
            AddressListItem(
                addressLabel,
                address,
                false,
                { callbackTriggered = true },
                modifier = Modifier.Companion.testTag(tag)
            )
        }

        // When address list item is clicked
        testRule.onNodeWithTag(tag).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }
}