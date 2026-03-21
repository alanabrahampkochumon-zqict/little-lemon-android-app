package com.littlelemon.application.address.presentation

import app.cash.turbine.test
import com.littlelemon.application.address.domain.usecase.GeocodeAddressUseCase
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.domain.usecase.GetLocationUseCase
import com.littlelemon.application.address.domain.usecase.ReverseGeocodeLocationUseCase
import com.littlelemon.application.address.domain.usecase.SaveAddressUseCase
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.presentation.UiText
import com.littlelemon.application.utils.AddressGenerator
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(StandardTestDispatcherRule::class)
class AddressViewModelTest {

    companion object {
        //        private const val DEBOUNCE_DELAY_MS = 1000L
        private const val NETWORK_LATENCY = 500L
        private const val ERROR_MESSAGE = "Something went wrong!"
        private val EXPECTED_ERROR = UiText.DynamicString(ERROR_MESSAGE)
    }

    private lateinit var getLocationUseCase: GetLocationUseCase
    private lateinit var getAddressUseCase: GetAddressUseCase
    private lateinit var saveAddressUseCase: SaveAddressUseCase
    private lateinit var geocodeAddressUseCase: GeocodeAddressUseCase
    private lateinit var reverseGeocodeLocationUseCase: ReverseGeocodeLocationUseCase

    private lateinit var viewModel: AddressViewModel

    private val geocodedAddress = AddressGenerator.generateGeocodedAddress()

    @BeforeEach
    fun setUp() {
        getLocationUseCase = mockk()
        saveAddressUseCase = mockk()
        getAddressUseCase = mockk()
        geocodeAddressUseCase = mockk()
        reverseGeocodeLocationUseCase = mockk()

        viewModel = AddressViewModel(
            getLocationUseCase,
            getAddressUseCase,
            geocodeAddressUseCase,
            reverseGeocodeLocationUseCase,
            saveAddressUseCase
        )

        coEvery { geocodeAddressUseCase.invoke(any()) } returns Resource.Success(geocodedAddress)
        coEvery { reverseGeocodeLocationUseCase.invoke(any()) } returns Resource.Success(
            geocodedAddress
        )
    }

    @Nested
    inner class StateChangeTests {

        @Test
        fun onLabelChange_toValidLabel_updatesTheState() = runTest {
            val label = "new label"

            viewModel.state.test {
                skipItems(1)
                // When label is updated
                viewModel.onAction(AddressActions.ChangeLabel(label))
                // Then state reflects the update
                val state = awaitItem()
                assertEquals(label, state.label)
            }
        }

        @Test
        fun onBuildingNameChange_toValidAddress_updatesTheState() = runTest {
            val buildingName = "building name"

            viewModel.state.test {
                skipItems(1)
                // When building name is updated
                viewModel.onAction(AddressActions.ChangeBuildingName(buildingName))
                // Then state reflects the update
                val state = awaitItem()
                assertEquals(buildingName, state.buildingName)
            }
        }

        @Test
        fun onStreetAddressChange_toValidStreetAddress_updatesTheState() =
            runTest {
                val streetAddress = "street address"

                viewModel.state.test {
                    skipItems(1)
                    // When street address is updated
                    viewModel.onAction(AddressActions.ChangeStreetAddress(streetAddress))
                    // Then state reflects the update
                    val state = awaitItem()
                    assertEquals(streetAddress, state.streetAddress)
                }
            }

        @Test
        fun onCityChange_toValidCity_updatesTheState() = runTest {
            // Arrange
            val city = "city"

            viewModel.state.test {
                skipItems(1)
                // When city is updated
                viewModel.onAction(AddressActions.ChangeCity(city))
                // Then state reflects the update
                val state = awaitItem()
                assertEquals(city, state.city)
            }
        }

        @Test
        fun onStateChange_toValidState_updatesTheState() = runTest {
            val stateName = "state"

            viewModel.state.test {
                skipItems(1)
                // When state is updated
                viewModel.onAction(AddressActions.ChangeState(stateName))
                // Then state reflects the update
                val state = awaitItem()
                assertEquals(stateName, state.state)
            }
        }

        @Test
        fun onPinCodeChange_toValidPinCode_updatesTheState() = runTest {
            // Arrange
            val pinCode = "pinCode"

            viewModel.state.test {
                skipItems(1)
                // When pin code is updated
                viewModel.onAction(AddressActions.ChangePinCode(pinCode))
                // Then state reflects the update
                val state = awaitItem()
                assertEquals(pinCode, state.pinCode)
            }
        }


        @Test
        fun onDefaultAddressChange_toTrue_updatesTheState() = runTest {

            viewModel.state.test {
                // Given a state with isDefaultAddress set to false
                val initialState = awaitItem()
                assertFalse { initialState.isDefaultAddress }

                // When isDefault changes to true
                viewModel.onAction(AddressActions.ChangeToDefaultAddress(true))

                // Then, the state is properly updated
                val finalState = awaitItem()
                assertTrue(finalState.isDefaultAddress)

            }
        }

        @Test
        fun onGetLocation_success_updatesTheState() = runTest {
            // Arrange
            val location = AddressGenerator.generateLocalLocation()
            coEvery { getLocationUseCase.invoke() } returns Resource.Success(data = location)

            viewModel.state.test {
                skipItems(1)
                // Act
                viewModel.onAction(AddressActions.GetLocation)
                runCurrent()
                println(awaitItem()) // Skip the loading state

                // Assert
                val state = awaitItem()
                println(state)
                assertEquals(location.longitude, state.longitude)
                assertEquals(location.latitude, state.latitude)

                cancelAndIgnoreRemainingEvents()
            }
        }


        @Test
        fun onRequestLocation_success_showsInfoAndTriggersLocationRetrievedEvent() = runTest {
            // Arrange
            coEvery { getLocationUseCase.invoke() } returns Resource.Success()

            viewModel.addressEvents.test {
                // Act
                viewModel.onAction(AddressActions.GetLocation)
                runCurrent()

                // Assert I
                assertIs<AddressEvents.ShowInfo>(awaitItem())
//                val job = launch { viewModel.addressEvent.collect { } }
                // Assert II
                assertIs<AddressEvents.LocationRetrieved>(awaitItem())
//                job.cancel()
            }


        }

        @Test
        fun onRequestLocation_failure_showError() = runTest {
            // Arrange
            coEvery { getLocationUseCase.invoke() } returns Resource.Failure(errorMessage = ERROR_MESSAGE)

            viewModel.addressEvents.test {
                // Act
                viewModel.onAction(AddressActions.GetLocation)
                runCurrent()

                // Assert that error is shown
                val event = awaitItem()
                assertIs<AddressEvents.ShowError>(event)
                assertEquals(EXPECTED_ERROR, event.errorMessage)
            }
        }


        @Test
        fun onRequestLocation_whileRequesting_showsLoader() = runTest {
            coEvery { getLocationUseCase.invoke() } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success()
            }

            viewModel.state.test {
                // Assert that loading is false in the beginning
                assertFalse(awaitItem().isLoading)

                viewModel.onAction(AddressActions.GetLocation)
                runCurrent()

                // Assert that loader is shown
                assertTrue(awaitItem().isLoading)

                advanceTimeBy(NETWORK_LATENCY + 1)

                // Assert that loading is reset in the end
                assertFalse(awaitItem().isLoading)
            }

        }

        @Test
        fun onEnterLocationManually_triggerLocationEntryPopup() = runTest {
            viewModel.addressEvents.test {
                // Arrange & Act
                viewModel.onAction(AddressActions.EnterLocationManually)
                runCurrent()

                // Assert that popup event is triggered
                assertIs<AddressEvents.ShowLocationEntryPopup>(awaitItem())
            }

        }
    }

    @Nested
    inner class StateErrorResetTests {

        @Test
        fun onBuildingNameChange_toBuildingName_resetsErrorState() = runTest {
            val newValue = "value"
            viewModel.state.test {
                skipItems(1)

                // Given a state with building name error
                viewModel.onAction(AddressActions.ChangeBuildingName(""))
                viewModel.onAction(AddressActions.SaveAddress)
                assertNotNull(awaitItem().buildingNameError)

                // When building name is updated
                viewModel.onAction(AddressActions.ChangeBuildingName(newValue))

                // Then error state is reset
                assertNull(awaitItem().buildingNameError)
            }
        }

        @Test
        fun onStreetAddressChange_toValidStreetAddress_resetsErrorState() = runTest {
            val newValue = "value"
            viewModel.state.test {
                skipItems(1)

                // Given a state with street address error
                viewModel.onAction(AddressActions.ChangeStreetAddress(""))
                viewModel.onAction(AddressActions.SaveAddress)
                assertNotNull(awaitItem().streetAddressError)

                // When street address is updated
                viewModel.onAction(AddressActions.ChangeStreetAddress(newValue))

                // Then error state is reset
                assertNull(awaitItem().streetAddressError)
            }
        }

        @Test
        fun onCityChange_toValidCity_resetsErrorState() = runTest {
            val newValue = "value"
            viewModel.state.test {
                skipItems(1)

                // Given a state with city error
                viewModel.onAction(AddressActions.ChangeCity(""))
                viewModel.onAction(AddressActions.SaveAddress)
                assertNotNull(awaitItem().cityError)

                // When city is updated
                viewModel.onAction(AddressActions.ChangeCity(newValue))

                // Then error state is reset
                assertNull(awaitItem().cityError)
            }
        }


        @Test
        fun onStateChange_toValidState_resetsErrorState() = runTest {
            val newValue = "value"
            viewModel.state.test {
                skipItems(1)

                // Given a state with state error
                viewModel.onAction(AddressActions.ChangeState(""))
                viewModel.onAction(AddressActions.SaveAddress)
                assertNotNull(awaitItem().stateError)

                // When state is updated
                viewModel.onAction(AddressActions.ChangeState(newValue))

                // Then error state is reset
                assertNull(awaitItem().stateError)
            }
        }


        @Test
        fun onPinCodeChange_toValidPinCode_resetsErrorState() = runTest {
            val newValue = "value"
            viewModel.state.test {
                skipItems(1)

                // Given a state with pin code error
                viewModel.onAction(AddressActions.ChangePinCode(""))
                viewModel.onAction(AddressActions.SaveAddress)
                assertNotNull(awaitItem().pinCodeError)

                // When pin code is updated
                viewModel.onAction(AddressActions.ChangePinCode(newValue))

                // Then error state is reset
                assertNull(awaitItem().pinCodeError)
            }
        }
    }

    @Nested
    inner class SaveAddressTests {

        @Test
        fun onSaveAddress_success_showInfoAndTriggersAddressSavedEvent() = runTest {
            // Given a viewmodel with valid fields
            viewModel.onAction(AddressActions.ChangeBuildingName("address"))
            viewModel.onAction(AddressActions.ChangeStreetAddress("street"))
            viewModel.onAction(AddressActions.ChangeCity("city"))
            viewModel.onAction(AddressActions.ChangeState("state"))
            viewModel.onAction(AddressActions.ChangePinCode("123456"))

            // and network success
            coEvery { saveAddressUseCase.invoke(any()) } returns Resource.Success()

            viewModel.addressEvents.test {
                // When address is saved
                viewModel.onAction(AddressActions.SaveAddress)

                // Then it triggers show info
                val firstEvent = awaitItem()
                assertIs<AddressEvents.ShowInfo>(firstEvent)

                // And address saved event
                val secondEvent = awaitItem()
                assertIs<AddressEvents.AddressSaved>(secondEvent)
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun saveAddressSuccess_dismissesDialog() = runTest {
            // Given the location dialog is displayed
            viewModel.onAction(AddressActions.ShowLocationDialog)
            viewModel.onAction(AddressActions.ChangeBuildingName("address"))
            viewModel.onAction(AddressActions.ChangeStreetAddress("street"))
            viewModel.onAction(AddressActions.ChangeCity("city"))
            viewModel.onAction(AddressActions.ChangeState("state"))
            viewModel.onAction(AddressActions.ChangePinCode("123456"))

            // and network request succeeds
            coEvery { saveAddressUseCase.invoke(any()) } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success()
            }

            viewModel.state.test {
                skipItems(1)

                // When address is saved
                viewModel.onAction(AddressActions.SaveAddress)

                // Then, it shows the dialog initially
                val initialState = awaitItem()
                assertTrue(initialState.showLocationDialog)

                // And then dismisses it
                val finalState = awaitItem()
                assertFalse(finalState.showLocationDialog)
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun saveAddressFailures_doesNotDismissDialog() = runTest {
            // Given the location dialog is displayed
            viewModel.onAction(AddressActions.ShowLocationDialog)
            viewModel.onAction(AddressActions.ChangeBuildingName("address"))
            viewModel.onAction(AddressActions.ChangeStreetAddress("street"))
            viewModel.onAction(AddressActions.ChangeCity("city"))
            viewModel.onAction(AddressActions.ChangeState("state"))
            viewModel.onAction(AddressActions.ChangePinCode("123456"))

            // and network request fails
            coEvery { saveAddressUseCase.invoke(any()) } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Failure()
            }

            viewModel.state.test {
                skipItems(1)

                // When address is saved
                viewModel.onAction(AddressActions.SaveAddress)

                // Then, it shows the dialog initially
                val initialState = awaitItem()
                assertTrue(initialState.showLocationDialog)

                // And doesn't dismiss it
                val finalState = awaitItem()
                assertTrue(finalState.showLocationDialog)
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun onSaveAddress_failure_showError() = runTest {
            // Given a viewmodel with valid fields
            viewModel.onAction(AddressActions.ChangeBuildingName("address"))
            viewModel.onAction(AddressActions.ChangeStreetAddress("street"))
            viewModel.onAction(AddressActions.ChangeCity("city"))
            viewModel.onAction(AddressActions.ChangeState("state"))
            viewModel.onAction(AddressActions.ChangePinCode("123456"))

            // But network fails
            coEvery { saveAddressUseCase.invoke(any()) } returns Resource.Failure(errorMessage = ERROR_MESSAGE)

            viewModel.addressEvents.test {
                // When address is saved
                viewModel.onAction(AddressActions.SaveAddress)

                // Then, error message is shown
                val event = awaitItem()
                assertIs<AddressEvents.ShowError>(event)
                assertEquals(EXPECTED_ERROR, event.errorMessage)
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun onSaveAddress_whileSaving_showsLoader() = runTest {
            // Given a viewmodel with valid fields
            viewModel.onAction(AddressActions.ChangeBuildingName("address"))
            viewModel.onAction(AddressActions.ChangeStreetAddress("street"))
            viewModel.onAction(AddressActions.ChangeCity("city"))
            viewModel.onAction(AddressActions.ChangeState("state"))
            viewModel.onAction(AddressActions.ChangePinCode("123456"))

            coEvery { saveAddressUseCase.invoke(any()) } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success()
            }

            viewModel.state.test {
                assertFalse(awaitItem().isLoading) // Initial Loading State

                // When address is saved
                viewModel.onAction(AddressActions.SaveAddress)

                // Then, loading state is first shown
                val loadingState = awaitItem()
                assertTrue(loadingState.isLoading)

                // And then dismissed
                val postLoadingState = awaitItem()
                assertFalse(postLoadingState.isLoading)
            }
        }

        @Test
        fun onShowDialog_updateStateToShowDialog() = runTest {

            viewModel.state.test {
                skipItems(1) // Ignore initial state

                // When show dialog action is triggered
                viewModel.onAction(AddressActions.ShowLocationDialog)

                // Then, the state is updated to showDialog = true
                assertTrue(awaitItem().showLocationDialog)
            }
        }

        @Test
        fun onDismissDialog_updateStateToDismissDialog() = runTest {
            viewModel.state.test {
                skipItems(1) // Ignore initial state

                // Set state to show dialog initially
                viewModel.onAction(AddressActions.ShowLocationDialog)

                // Assert that dialog is shown
                assertTrue(awaitItem().showLocationDialog)

                // When dismiss dialog is triggered
                viewModel.onAction(AddressActions.DismissLocationDialog)

                // Then, the state is updated to showDialog = false
                assertFalse(awaitItem().showLocationDialog)
            }
        }


        @Test
        fun saveAddress_blankBuildingName_showsErrorMessage() = runTest {
            // Given a viewmodel with blank building name
            viewModel.onAction(AddressActions.ChangeBuildingName(""))
            viewModel.state.test {
                skipItems(1) // Ignore initial state
                // When save address is pressed
                viewModel.onAction(AddressActions.SaveAddress)

                // Then it shows an error
                val state = awaitItem()
                assertNotNull(state.buildingNameError)
            }
        }

        @Test
        fun saveAddress_blankStreetAddress_showsErrorMessage() = runTest {
            // Given a viewmodel with blank street address
            viewModel.onAction(AddressActions.ChangeStreetAddress(""))
            viewModel.state.test {
                skipItems(1) // Ignore initial state
                // When save address is pressed
                viewModel.onAction(AddressActions.SaveAddress)

                // Then it shows an error
                val state = awaitItem()
                assertNotNull(state.streetAddressError)
            }
        }

        @Test
        fun saveAddress_blankCity_showsErrorMessage() = runTest {
            // Given a viewmodel with blank city
            viewModel.onAction(AddressActions.ChangeCity(""))
            viewModel.state.test {
                skipItems(1) // Ignore initial state
                // When save address is pressed
                viewModel.onAction(AddressActions.SaveAddress)

                // Then it shows an error
                val state = awaitItem()
                assertNotNull(state.cityError)
            }
        }

        @Test
        fun saveAddress_blankState_showsErrorMessage() = runTest {
            // Given a viewmodel with blank state
            viewModel.onAction(AddressActions.ChangeState(""))
            viewModel.state.test {
                skipItems(1) // Ignore initial state
                // When save address is pressed
                viewModel.onAction(AddressActions.SaveAddress)

                // Then it shows an error
                val state = awaitItem()
                assertNotNull(state.stateError)
            }
        }

        @Test
        fun saveAddress_blankPinCode_showsErrorMessage() = runTest {
            // Given a viewmodel with blank pin code
            viewModel.onAction(AddressActions.ChangePinCode(""))
            viewModel.state.test {
                skipItems(1) // Ignore initial state
                // When save address is pressed
                viewModel.onAction(AddressActions.SaveAddress)

                // Then it shows an error
                val state = awaitItem()
                assertNotNull(state.pinCodeError)
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun saveAddress_withAllFieldsBlank_showsIndividualError() = runTest {
            // Given a viewmodel with all required fields blank
            viewModel.onAction(AddressActions.ChangeBuildingName(""))
            viewModel.onAction(AddressActions.ChangeStreetAddress(""))
            viewModel.onAction(AddressActions.ChangeCity(""))
            viewModel.onAction(AddressActions.ChangeState(""))
            viewModel.onAction(AddressActions.ChangePinCode(""))
            viewModel.state.test {
                skipItems(1) // Ignore initial state
                // When save address is pressed
                viewModel.onAction(AddressActions.SaveAddress)

                // Then it shows an error
                val state = awaitItem()
                assertNotNull(state.buildingNameError)
                assertNotNull(state.streetAddressError)
                assertNotNull(state.cityError)
                assertNotNull(state.stateError)
                assertNotNull(state.pinCodeError)
            }
        }
    }

    @Nested
    inner class SaveLocationTests {


        // TODO: Delegate to separate function
//        @Test
//        fun saveLocation_emptyAddressFields_fillAddressFields() = runTest {
//            coEvery {
//                saveAddressUseCase.invoke(any())
//            } coAnswers {
//                delay(NETWORK_LATENCY)
//                Resource.Success()
//            }
//            viewModel.onAction(AddressActions.ChangeBuildingName(""))
//            viewModel.onAction(AddressActions.ChangeStreetAddress(""))
//            viewModel.onAction(AddressActions.ChangeCity(""))
//            viewModel.onAction(AddressActions.ChangeState(""))
//            viewModel.onAction(AddressActions.ChangePinCode(""))
//
//            viewModel.state.test {
//                skipItems(1)
//
//                // When the location is saved
//                viewModel.onAction(AddressActions.SaveLocation)
//
//                // Then, after initial loading
//                skipItems(1)
//
//                // Addresses are filled in
//                val state = awaitItem()
//                assertEquals(geocodedAddress.address?.address, state.buildingName)
//                assertEquals(geocodedAddress.address?.streetAddress, state.streetAddress)
//                assertEquals(geocodedAddress.address?.state, state.state)
//                assertEquals(geocodedAddress.address?.city, state.city)
//                assertEquals(geocodedAddress.address?.state, state.state)
//                assertEquals(geocodedAddress.address?.pinCode, state.pinCode)
//
//            }

        @Test
        fun saveLocation_setLoadingToTrueWhileSavingAndToFalseAfterwards() = runTest {
            coEvery {
                saveAddressUseCase.invoke(any())
            } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success()
            }

            viewModel.state.test {
                skipItems(1)

                // When the location is saved
                viewModel.onAction(AddressActions.SaveLocation)

                // Then, state is set to loading initially
                assertTrue(awaitItem().isLoading)

                // And, set to false afterward
                assertFalse(awaitItem().isLoading)

            }
        }

        @Test
        fun saveLocationSuccess_triggerAddressSavedEvent() = runTest {
            // Given network success
            coEvery {
                saveAddressUseCase.invoke(any())
            } returns Resource.Success()

            viewModel.addressEvents.test {
                // When address is saved
                viewModel.onAction(AddressActions.SaveLocation)

                // Then, address saved event is triggered
                assertIs<AddressEvents.AddressSaved>(awaitItem())
            }
        }

        @Test
        fun saveLocationFailure_triggersShowErrorEvent() = runTest {
            // Given network failure
            coEvery {
                saveAddressUseCase.invoke(any())
            } returns Resource.Failure()

            viewModel.addressEvents.test {
                // When address is saved
                viewModel.onAction(AddressActions.SaveLocation)

                // Then, address saved event is triggered
                assertIs<AddressEvents.ShowError>(awaitItem())
            }
        }
    }

}