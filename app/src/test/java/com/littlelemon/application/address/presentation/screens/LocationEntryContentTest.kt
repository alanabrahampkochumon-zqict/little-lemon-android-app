package com.littlelemon.application.address.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class LocationEntryContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val application = RuntimeEnvironment.getApplication()

    private val labelLabelMatcher = hasText(application.getString(R.string.label_address_label))
    private val labelPlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_label))

    private val buildingLabelMatcher =
        hasText(application.getString(R.string.label_address_building_name))
    private val buildingPlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_building_name))

    private val streetAddressLabelMatcher =
        hasText(application.getString(R.string.label_address_street_address))
    private val streetAddressPlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_street_address))

    private val cityLabelMatcher = hasText(application.getString(R.string.label_address_city))
    private val cityPlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_city))

    private val stateLabelMatcher = hasText(application.getString(R.string.label_address_state))
    private val statePlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_state))

    private val pinCodeLabelMatcher = hasText(application.getString(R.string.label_address_pincode))
    private val pinCodePlaceholderMatcher =
        hasText(application.getString(R.string.placeholder_address_pincode))

    private val saveAsDefaultLabelMatcher =
        hasText(application.getString(R.string.label_address_save_as_default))
    private val saveAsDefaultTestTag =
        hasTestTag(application.getString(R.string.test_tag_address_save_as_default))


    @Test
    fun locationEntryContent_label_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then, "label" label and its field is displayed
        composeTestRule.onNode(labelLabelMatcher).assertIsDisplayed()
        composeTestRule.onNode(labelPlaceholderMatcher).assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_buildingName_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then, "building name" label and its field is displayed
        composeTestRule.onNode(buildingLabelMatcher).assertIsDisplayed()
        composeTestRule.onNode(buildingPlaceholderMatcher).assertIsDisplayed()
    }


    @Test
    fun locationEntryContent_streetAddress_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "street address" label and its field is displayed
        composeTestRule.onNode(streetAddressLabelMatcher).assertIsDisplayed()
        composeTestRule.onNode(streetAddressPlaceholderMatcher).assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_city_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "city" label and its field is displayed
        composeTestRule.onNode(cityLabelMatcher).assertIsDisplayed()
        composeTestRule.onNode(cityPlaceholderMatcher).assertIsDisplayed()
    }


    @Test
    fun locationEntryContent_state_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "state" label and its field is displayed
        composeTestRule.onNode(stateLabelMatcher).assertIsDisplayed()
        composeTestRule.onNode(statePlaceholderMatcher).assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_pinCode_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "pincode" label and its field is displayed
        composeTestRule.onNode(pinCodeLabelMatcher).assertIsDisplayed()
        composeTestRule.onNode(pinCodePlaceholderMatcher).assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_saveAsDefault_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then "pincode" label and its field is displayed
        composeTestRule.onNode(saveAsDefaultLabelMatcher).assertIsDisplayed()
        composeTestRule.onNode(saveAsDefaultTestTag).assertIsDisplayed()
    }

}