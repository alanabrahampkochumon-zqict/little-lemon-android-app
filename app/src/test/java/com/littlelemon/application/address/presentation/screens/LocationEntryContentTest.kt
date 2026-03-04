package com.littlelemon.application.address.presentation.screens

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import com.google.testing.junit.testparameterinjector.TestParameter
import com.littlelemon.application.R
import com.littlelemon.application.address.presentation.AddressState
import com.littlelemon.application.address.presentation.AddressTestTags
import com.littlelemon.application.utils.ComposeMatcherHelper
import com.littlelemon.application.utils.MatcherType
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestParameterInjector
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestParameterInjector::class)
class LocationEntryContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val application by lazy { RuntimeEnvironment.getApplication() }

    private val composeMatchHelper = ComposeMatcherHelper(application)

    enum class ContentMatchTestCase(
        val label: Int,
        val placeholder: Int,
        val labelMatcherType: MatcherType = MatcherType.Text,
        val placeholderMatcherType: MatcherType = MatcherType.Text
    ) {
        LABEL(R.string.label_address_label, R.string.placeholder_address_label),
        BUILDING_NAME(
            R.string.label_address_building_name,
            R.string.placeholder_address_building_name
        ),
        STREET_ADDRESS(
            R.string.label_address_street_address,
            R.string.placeholder_address_street_address
        ),
        CITY(R.string.label_address_city, R.string.placeholder_address_city),
        STATE(R.string.label_address_state, R.string.placeholder_address_state),
        PIN_CODE(R.string.label_address_pincode, R.string.placeholder_address_pincode),
        SAVE_AS_DEFAULT(
            R.string.label_address_save_as_default,
            R.string.test_tag_address_save_as_default,
            placeholderMatcherType = MatcherType.TestTag
        )
    }

    private fun getInputField(nodeType: ContentMatchTestCase): SemanticsNodeInteraction {
        return composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                nodeType.placeholder,
                nodeType.placeholderMatcherType
            )
        )
    }

    private val labelField by lazy {
        getInputField(ContentMatchTestCase.LABEL)
    }

    private val buildingNameField by lazy {
        getInputField(ContentMatchTestCase.BUILDING_NAME)
    }

    private val streetAddressField by lazy {
        getInputField(ContentMatchTestCase.STREET_ADDRESS)

    }
    private val cityField by lazy {
        getInputField(ContentMatchTestCase.CITY)

    }
    private val stateField by lazy {
        getInputField(ContentMatchTestCase.STATE)

    }
    private val pinCodeField by lazy {
        getInputField(ContentMatchTestCase.PIN_CODE)
    }

    private val saveAsDefault by lazy {
        composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                ContentMatchTestCase.SAVE_AS_DEFAULT.label,
                ContentMatchTestCase.SAVE_AS_DEFAULT.labelMatcherType
            )
        )
    }


    @Test
    fun locationEntryContent_fields_areDisplayed(
        @TestParameter case: ContentMatchTestCase
    ) {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then, an input field's label and its input is displayed
        getInputField(case)
            .performScrollTo()
            .assertIsDisplayed()
        composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                case.label,
                case.labelMatcherType
            )
        ).performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun locationEntryContent_imeActionOnLabel_focusesBuildingName() {
        // Given a location entry screen with label field focused
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }
        labelField.performClick()

        // When ime action is performed
        labelField.performImeAction()

        // Then, the next field is focused
        labelField.assertIsNotFocused()
        buildingNameField.assertIsFocused()
    }

    @Test
    fun locationEntryContent_imeActionOnBuildingName_focusesStreetAddress() {
        // Given a location entry screen with building name focused
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }
        buildingNameField.performClick()

        // When ime action is performed
        buildingNameField.performImeAction()

        // Then, the next field is focused
        buildingNameField.assertIsNotFocused()
        streetAddressField.assertIsFocused()
    }

    @Test
    fun locationEntryContent_imeActionOnStreetAddress_focusesCity() {
        // Given a location entry screen with street address focused
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }
        streetAddressField.performClick()

        // When ime action is performed
        streetAddressField.performImeAction()

        // Then, the next field is focused
        streetAddressField.assertIsNotFocused()
        cityField.assertIsFocused()
    }

    @Test
    fun locationEntryContent_imeActionOnCity_focusesState() {
        // Given a location entry screen with city focused
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }
        cityField.performClick()

        // When ime action is performed
        cityField.performImeAction()

        // Then, the next field is focused
        cityField.assertIsNotFocused()
        stateField.assertIsFocused()
    }

    @Test
    fun locationEntryContent_imeActionState_focusesPinCode() {
        // Given a location entry screen with state focused
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }
        stateField.performClick()

        // When ime action is performed
        stateField.performImeAction()

        // Then, the next field is focused
        stateField.assertIsNotFocused()
        pinCodeField.assertIsFocused()
    }

    @Test
    fun locationEntryContent_imeActionState_triggersOnSaveAddressCallback() {
        // Given a location entry screen with pin code focused
        var callbackTriggered = false
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onSaveAddress = { callbackTriggered = true })
        }
        pinCodeField.performClick()

        // When ime action is performed
        pinCodeField.performImeAction()

        // Then, the next field is focused
        stateField.assertIsNotFocused()
        assertTrue(callbackTriggered)
    }


    @Test
    fun locationEntryContent_labelChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen

        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onLabelChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                ContentMatchTestCase.LABEL.placeholder,
                ContentMatchTestCase.LABEL.placeholderMatcherType
            )
        ).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)

    }

    @Test
    fun locationEntryContent_buildingNameChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onBuildingNameChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                ContentMatchTestCase.BUILDING_NAME.placeholder,
                ContentMatchTestCase.BUILDING_NAME.placeholderMatcherType
            )
        ).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)

    }

    @Test
    fun locationEntryContent_streetAddressChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onStreetAddressChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                ContentMatchTestCase.STREET_ADDRESS.placeholder,
                ContentMatchTestCase.STREET_ADDRESS.placeholderMatcherType
            )
        ).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)

    }

    @Test
    fun locationEntryContent_cityChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onCityChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                ContentMatchTestCase.CITY.placeholder,
                ContentMatchTestCase.CITY.placeholderMatcherType
            )
        ).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)
    }

    @Test
    fun locationEntryContent_stateChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onStateChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                ContentMatchTestCase.STATE.placeholder,
                ContentMatchTestCase.STATE.placeholderMatcherType
            )
        ).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)
    }

    @Test
    fun locationEntryContent_pinCodeChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        val content = "content"
        var changedValue = ""
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onPinCodeChange = { changedValue = it })
        }

        // When text input is performed
        composeTestRule.onNode(
            composeMatchHelper.getMatcher(
                ContentMatchTestCase.PIN_CODE.placeholder,
                ContentMatchTestCase.PIN_CODE.placeholderMatcherType
            )
        ).performTextInput(content)

        // Then, the callback is triggered
        assertEquals(content, changedValue)
    }

    @Test
    fun locationEntryScreen_saveAddressClicked_triggersOnSaveAddressCallback() {
        // Given a location entry screen
        var callbackTriggered = false
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onSaveAddress = { callbackTriggered = true })
        }

        // When Save address button is pressed
        composeTestRule.onNodeWithText(application.getString(R.string.act_save_address))
            .performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun locationEntryScreen_cancelClicked_triggersOnCloseCallback() {
        // Given a location entry screen
        var callbackTriggered = false
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onClose = { callbackTriggered = true })
        }

        // When Save address button is pressed
        composeTestRule.onNodeWithText(application.getString(R.string.act_cancel))
            .performClick()

        // Then, callback is triggered
        assertTrue(callbackTriggered)
    }


    @Test
    fun locationEntryContent_saveAsDefaultChange_onChangeCallbackIsTriggered() {
        // Given a location entry screen
        var callbackTriggered = false
        composeTestRule.setContent {
            LocationEntryContentRoot(
                AddressState(),
                onSaveAsDefaultChange = { callbackTriggered = true })
        }

        // When text input is performed
        saveAsDefault.performSemanticsAction(SemanticsActions.OnClick)

        // Then the callback is triggered
        assertTrue(callbackTriggered)
    }

    @Test
    fun locationEntryScreen_header_isDisplayed() {
        // Given a location entry screen
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState())
        }

        // Then, the header is displayed
        composeTestRule.onNodeWithText(application.getString(R.string.heading_add_your_address))
            .assertIsDisplayed()
    }

    @Test
    fun locationEntryScreen_closeButtonClick_triggersOnCloseCallback() {
        // Given a location entry screen
        var callbackTriggered = false
        composeTestRule.setContent {
            LocationEntryContentRoot(AddressState(), onClose = { callbackTriggered = true })
        }

        // When, close button is pressed
        composeTestRule.onNodeWithTag(AddressTestTags.HEADER_CLOSE_BUTTON).performClick()

        // Then, the callback is triggered
        assertTrue(callbackTriggered)
    }

    // TODO: Refactor test
//    @Test
//    fun locationEntryScreen_notFloating_whenScrolledReducesHeaderSize() {
//        // Given the location entry screen
//        composeTestRule.setContent {
//            LocationEntryContentRoot(AddressState(), isFloating = false)
//        }
//
//        // When the scrollview is scrolled
//        composeTestRule.onNodeWithTag(AddressTestTags.NESTED_SCROLL_VIEW).performScrollTo()
//
//        // Then the height of the header section changes
//        composeTestRule.onNodeWithTag(AddressTestTags.NESTED_SCROLL_HEADER)
//            .assertHeightIsAtLeast(100.dp)
//    }

}