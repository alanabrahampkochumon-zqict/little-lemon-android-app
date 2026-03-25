package com.littlelemon.application.address.presentation.screens.components

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.presentation.AddressTestTags
import com.littlelemon.application.address.presentation.mappers.toFullAddress
import com.littlelemon.application.utils.AddressGenerator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class AddressListTest {

    @get:Rule
    val testRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    private val addressList = List(5) { AddressGenerator.generateLocalAddress() }
    private val selectedAddress = addressList[0]

    @Test
    fun maximumThreeAddressDisplayed() {
        // Given an address list
        testRule.setContent {
            AddressList(addressList, selectedAddress, {}, {})
        }

        // Then, a max of 3 items are only displayed.
        val nodes = testRule.onAllNodesWithTag(AddressTestTags.ADDRESS_ITEM)
        nodes.assertCountEquals(3)
    }

    @Test
    fun selectedAddressIsDisplayed() {
        // Given an address list
        testRule.setContent {
            AddressList(addressList, selectedAddress, {}, {})
        }

        // Then the selected address is displayed
        testRule.onNodeWithText(selectedAddress.address?.toFullAddress()!!).assertIsDisplayed()
    }

    @Test
    fun twoUnselectedAddressIsDisplayed() {
        // Given an address list
        testRule.setContent {
            AddressList(addressList, selectedAddress, {}, {})
        }

        // Then, two unselected addresses are displayed
        testRule.onNodeWithText(addressList[1].address?.toFullAddress()!!).assertIsDisplayed()
        testRule.onNodeWithText(addressList[2].address?.toFullAddress()!!).assertIsDisplayed()
    }

    @Test
    fun viewAllButtonIsDisplayed() {
        // Given an address list
        testRule.setContent {
            AddressList(addressList, selectedAddress, {}, {})
        }

        // Then view all button is displayed
        testRule.onNodeWithText(application.getString(R.string.view_all)).assertIsDisplayed()
    }

    @Test
    fun viewAllClick_triggersCallback() {
        // Given an address list
        var callbackTriggered = false
        testRule.setContent {
            AddressList(
                addressList,
                selectedAddress,
                {},
                onViewAllAddress = { callbackTriggered = true })
        }

        // When view all button is clicked
        testRule.onNodeWithText(application.getString(R.string.view_all)).performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun onSelectionChange_triggersCallback() {
        // Given an address list
        var callbackTriggeredWith: LocalAddress? = null
        testRule.setContent {
            AddressList(
                addressList,
                selectedAddress,
                { callbackTriggeredWith = it },
                onViewAllAddress = { })
        }

        // When an address is selected is clicked
        testRule.onNodeWithText(addressList[1].address?.toFullAddress()!!).performClick()

        // Then, callback is triggered with correct address
        assertEquals(addressList[1], callbackTriggeredWith)
    }


}