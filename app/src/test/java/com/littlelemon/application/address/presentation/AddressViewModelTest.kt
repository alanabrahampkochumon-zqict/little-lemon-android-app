package com.littlelemon.application.address.presentation

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.littlelemon.application.address.domain.models.LocalAddress
import com.littlelemon.application.address.domain.models.LocalLocation
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
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

    private companion object {
        //        private const val DEBOUNCE_DELAY_MS = 1000L
        const val NETWORK_LATENCY = 500L
        const val ERROR_MESSAGE = "Something went wrong!"
        val EXPECTED_ERROR = UiText.DynamicString(ERROR_MESSAGE)

        val address = List(10) { AddressGenerator.generateLocalAddress() }
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
        getAddressUseCase = mockk<GetAddressUseCase>()
        geocodeAddressUseCase = mockk()
        reverseGeocodeLocationUseCase = mockk()

        coEvery { geocodeAddressUseCase.invoke(any()) } returns Resource.Success(geocodedAddress)
        coEvery { reverseGeocodeLocationUseCase.invoke(any()) } returns Resource.Success(
            geocodedAddress
        )
        coEvery { getAddressUseCase.invoke() } returns flow {
            emit(Resource.Loading())
            delay(NETWORK_LATENCY)
            emit(Resource.Success(address))
        }

        viewModel = AddressViewModel(
            getLocationUseCase,
            getAddressUseCase,
            geocodeAddressUseCase,
            reverseGeocodeLocationUseCase,
            saveAddressUseCase
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
    }

    @Nested
    inner class GetLocationTests {

        @Test
        fun success_updatesTheState() = runTest {
            // Arrange
            val location = AddressGenerator.generateLocalLocation()
            coEvery { getLocationUseCase.invoke() } returns Resource.Success(data = location)

            viewModel.state.test {
                skipItems(1)

                // Act
                viewModel.onAction(AddressActions.GetLocation)
                skipItems(1) // Skip the loading state

                // Assert
                val state = awaitItem()
                assertEquals(location.longitude, state.longitude)
                assertEquals(location.latitude, state.latitude)
            }
        }


        @Test
        fun success_showsInfoAndTriggersLocationRetrievedEvent() = runTest {
            // Arrange
            coEvery { getLocationUseCase.invoke() } returns Resource.Success()

            viewModel.addressEvents.test {
                // Act
                viewModel.onAction(AddressActions.GetLocation)
                runCurrent()

                // Assert I
                assertIs<AddressEvents.ShowInfo>(awaitItem())
                // Assert II
                assertIs<AddressEvents.LocationRetrievalSuccess>(awaitItem())
            }


        }

        @Test
        fun failure_showError() = runTest {
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
        fun whileRequesting_showsLoader() = runTest {
            coEvery { getLocationUseCase.invoke() } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success(AddressGenerator.generateLocalLocation())
            }

            viewModel.state.test {
                // Assert that loading is false in the beginning
                assertFalse(awaitItem().isLoading)

                viewModel.onAction(AddressActions.GetLocation)
                // Assert that loader is shown
                assertTrue(awaitItem().isLoading)
                advanceUntilIdle()

                // Assert that loading is reset in the end
                assertFalse(awaitItem().isLoading)
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
                assertIs<AddressEvents.AddressSaveSuccess>(secondEvent)
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
                assertIs<AddressEvents.AddressSaveSuccess>(awaitItem())
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

    @Nested
    inner class ReverseGeocodingTests {

        @Test
        fun success_updatesFieldsIfEmpty() = runTest {
            // Given empty fields
            viewModel.onAction(AddressActions.ChangeBuildingName(""))
            viewModel.onAction(AddressActions.ChangeStreetAddress(""))
            viewModel.onAction(AddressActions.ChangeCity(""))
            viewModel.onAction(AddressActions.ChangeState(""))
            viewModel.onAction(AddressActions.ChangePinCode(""))
            coEvery { getLocationUseCase.invoke() } returns Resource.Success(
                LocalLocation(
                    1.234,
                    4.568
                )
            )

            viewModel.state.test {
                skipItems(1)
                viewModel.onAction(AddressActions.GetLocation)
                advanceUntilIdle()
                skipItems(2) // Skip the loading and success state

                // When location is reverse geocoded
                viewModel.onAction(AddressActions.ReverseGeocodeLocation)
                advanceUntilIdle()
                skipItems(1) // Skip the loading state

                // Addresses are filled in
                val state = awaitItem()
                assertEquals(geocodedAddress.address?.address, state.buildingName)
                assertEquals(geocodedAddress.address?.streetAddress, state.streetAddress)
                assertEquals(geocodedAddress.address?.state, state.state)
                assertEquals(geocodedAddress.address?.city, state.city)
                assertEquals(geocodedAddress.address?.pinCode, state.pinCode)
            }
        }


        @Test
        fun success_doesNotUpdateNonEmptyFields() = runTest {
            // Given some non-empty fields
            val stateName = "state"
            val buildingName = "building name"
            viewModel.onAction(AddressActions.ChangeBuildingName(buildingName))
            viewModel.onAction(AddressActions.ChangeStreetAddress(""))
            viewModel.onAction(AddressActions.ChangeCity(""))
            viewModel.onAction(AddressActions.ChangeState(stateName))
            viewModel.onAction(AddressActions.ChangePinCode(""))
            coEvery { getLocationUseCase.invoke() } returns Resource.Success(
                LocalLocation(
                    1.234,
                    4.568
                )
            )

            viewModel.state.test {
                skipItems(1)
                viewModel.onAction(AddressActions.GetLocation)
                advanceUntilIdle()
                skipItems(2) // Skip the loading and success state

                // When location is reverse geocoded
                viewModel.onAction(AddressActions.ReverseGeocodeLocation)
                advanceUntilIdle()
                skipItems(1) // Skip the loading state


                val state = awaitItem()
                // Filled in values are not overridden
                assertEquals(buildingName, state.buildingName)
                assertEquals(stateName, state.state)

                // Empty fields are filled
                assertEquals(geocodedAddress.address?.streetAddress, state.streetAddress)
                assertEquals(geocodedAddress.address?.city, state.city)
                assertEquals(geocodedAddress.address?.pinCode, state.pinCode)
            }
        }

        @Test
        fun success_triggersSuccessMessageEvent() = runTest {
            // Given network success
            viewModel.onAction(AddressActions.ChangeBuildingName(""))
            viewModel.onAction(AddressActions.ChangeStreetAddress(""))
            viewModel.onAction(AddressActions.ChangeCity(""))
            viewModel.onAction(AddressActions.ChangeState(""))
            viewModel.onAction(AddressActions.ChangePinCode(""))

            coEvery { getLocationUseCase.invoke() } returns Resource.Success(
                LocalLocation(
                    1.234,
                    4.568
                )
            )
            viewModel.addressEvents.test {
                viewModel.onAction(AddressActions.GetLocation)
                skipItems(2) // Skip the success event from get location + show info event

                // When location is reverse geocoded
                viewModel.onAction(AddressActions.ReverseGeocodeLocation)

                // Then, show info event is triggered
                assertIs<AddressEvents.ShowInfo>(awaitItem())
                cancelAndConsumeRemainingEvents() // Consume geocoding success event
            }
        }

        @Test
        fun failure_triggersErrorMessageEvent() = runTest {
            // Given network failure
            coEvery {
                reverseGeocodeLocationUseCase.invoke(any())
            } coAnswers {
                Resource.Failure()
            }
            viewModel.onAction(AddressActions.ChangeBuildingName(""))
            viewModel.onAction(AddressActions.ChangeStreetAddress(""))
            viewModel.onAction(AddressActions.ChangeCity(""))
            viewModel.onAction(AddressActions.ChangeState(""))
            viewModel.onAction(AddressActions.ChangePinCode(""))
            coEvery { getLocationUseCase.invoke() } returns Resource.Success(
                LocalLocation(
                    1.234,
                    4.568
                )
            )
            viewModel.addressEvents.test {
                // When location is reverse geocoded
                viewModel.onAction(AddressActions.ReverseGeocodeLocation)

                // Then, show error event is triggered
                assertIs<AddressEvents.ShowError>(awaitItem())
            }
        }

        @Test
        fun whileLoading_loadingIsTrueAndFalseAfterwards() = runTest {
            // Given network failure
            coEvery {
                reverseGeocodeLocationUseCase.invoke(any())
            } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Failure()
            }

            coEvery { getLocationUseCase.invoke() } returns Resource.Success(
                LocalLocation(
                    1.234,
                    4.568
                )
            )

            viewModel.state.test {
                skipItems(1) // Skip the initial state
                viewModel.onAction(AddressActions.GetLocation)
                advanceUntilIdle()
                skipItems(2) // Skip the loading and success state

                // When location is reverse geocoded
                viewModel.onAction(AddressActions.ReverseGeocodeLocation)

                // Loading is first true
                assertTrue(awaitItem().isLoading) // Loading state
                advanceUntilIdle()

                // And then false
                assertFalse(awaitItem().isLoading) // Stop loading (Error)
            }
        }
    }

    @Nested
    inner class GeocodingTests {

        @Test
        fun success_updatesLocationIfEmpty() = runTest {
            // Given empty lat lng
            viewModel.onAction(AddressActions.ChangeBuildingName("building name"))
            coEvery { getLocationUseCase.invoke() } returns Resource.Success()

            viewModel.state.test {
                skipItems(1)
                viewModel.onAction(AddressActions.GetLocation)
                println(awaitItem())
                println(awaitItem())
//                skipItems(2) // Skip the loading and success state

                // When address is geocoded
                viewModel.onAction(AddressActions.GeocodeAddress)
                print(awaitItem()) // Skip the loading state

                // Latitude and Longitude is filled in
                val state = awaitItem()
                assertEquals(geocodedAddress.location?.latitude, state.latitude)
                assertEquals(geocodedAddress.location?.longitude, state.longitude)
            }
        }


        @Test
        fun success_doesNotUpdateIfLatLngAreNonNull() = runTest {
            // Given non empty lat lng
            val latitude = 1.234
            val longitude = 2.352
            viewModel.onAction(AddressActions.ChangeBuildingName("building name"))
            coEvery { getLocationUseCase.invoke() } returns Resource.Success(
                LocalLocation(
                    latitude,
                    longitude
                )
            )

            viewModel.state.test {
                skipItems(1)
                viewModel.onAction(AddressActions.GetLocation)
                advanceUntilIdle()
                skipItems(2) // Skip the loading and success state

                // When address is geocoded
                viewModel.onAction(AddressActions.GeocodeAddress)
                skipItems(1) // Skip the loading state
                advanceUntilIdle()

                // Latitude and Longitude is filled in
                val state = awaitItem()
                assertEquals(latitude, state.latitude)
                assertEquals(longitude, state.longitude)
            }
        }

        @Test
        fun success_triggersSuccessMessageEvent() = runTest {
            // Given network success
            viewModel.onAction(AddressActions.ChangeBuildingName("building name"))
            viewModel.onAction(AddressActions.ChangeStreetAddress("street address"))
            viewModel.onAction(AddressActions.ChangeCity("city"))
            viewModel.onAction(AddressActions.ChangeState("state"))
            viewModel.onAction(AddressActions.ChangePinCode("123456"))

            viewModel.addressEvents.test {
                // When an address is geocoded
                viewModel.onAction(AddressActions.GeocodeAddress)

                // Then, show info event is triggered
                assertIs<AddressEvents.ShowInfo>(awaitItem())
                cancelAndConsumeRemainingEvents() // Consume geocoding success event
            }
        }

        @Test
        fun success_triggersSuccessEventAfterMessageEvent() = runTest {
            // Given network success
            viewModel.onAction(AddressActions.ChangeBuildingName("building name"))
            viewModel.onAction(AddressActions.ChangeStreetAddress("street address"))
            viewModel.onAction(AddressActions.ChangeCity("city"))
            viewModel.onAction(AddressActions.ChangeState("state"))
            viewModel.onAction(AddressActions.ChangePinCode("123456"))

            viewModel.addressEvents.test {
                // When an address is geocoded
                viewModel.onAction(AddressActions.GeocodeAddress)
                skipItems(1) // Message event
                // Then, show info event is triggered
                assertIs<AddressEvents.GeocodeSuccess>(awaitItem())
            }
        }

        @Test
        fun failure_triggersErrorMessageEvent() = runTest {
            // Given network failure
            coEvery {
                geocodeAddressUseCase.invoke(any())
            } coAnswers {
                Resource.Failure()
            }
            viewModel.onAction(AddressActions.ChangeBuildingName("building name"))
            viewModel.onAction(AddressActions.ChangeStreetAddress("street address"))
            viewModel.onAction(AddressActions.ChangeCity("city"))
            viewModel.onAction(AddressActions.ChangeState("state"))
            viewModel.onAction(AddressActions.ChangePinCode("123456"))

            viewModel.addressEvents.test {
                // When address is geocoded
                viewModel.onAction(AddressActions.GeocodeAddress)

                // Then, show error event is triggered
                assertIs<AddressEvents.ShowError>(awaitItem())
            }
        }

        @Test
        fun whileLoading_loadingIsTrueAndFalseAfterwards() = runTest {
            // Given network failure
            viewModel.onAction(AddressActions.ChangeBuildingName("building name"))
            coEvery {
                geocodeAddressUseCase.invoke(any())
            } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success()
            }

            viewModel.state.test {
                assertFalse(awaitItem().isLoading) // Not loading before action is invoked

                // When an address is geocoded
                viewModel.onAction(AddressActions.GeocodeAddress)

                // Loading is first true
                assertTrue(awaitItem().isLoading) // Loading state
                advanceUntilIdle()

                // And then false
                assertFalse(awaitItem().isLoading) // Stop loading (Error)
            }
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

    @Nested
    inner class GetAddressTests {
        private lateinit var viewModel: AddressViewModel

        @Test
        fun isLoadingFirst() = runTest {
            turbineScope {
                // When an address viewmodel is created
                viewModel = AddressViewModel(
                    getLocationUseCase,
                    getAddressUseCase,
                    geocodeAddressUseCase,
                    reverseGeocodeLocationUseCase,
                    saveAddressUseCase
                )
                // Then loading state is emitted
                viewModel.addresses.test {
                    val loadingState = awaitItem() // Loading state
                    assertIs<Resource.Loading<List<LocalAddress>>>(loadingState)
                    assertNull(loadingState.data)
                }
            }
        }

        @Test
        fun onUseCaseSuccess_returnsSuccessWithResults() = runTest {
            turbineScope {
                // When an address viewmodel is created
                // And use case returns success
                viewModel = AddressViewModel(
                    getLocationUseCase,
                    getAddressUseCase,
                    geocodeAddressUseCase,
                    reverseGeocodeLocationUseCase,
                    saveAddressUseCase
                )
                viewModel.addresses.test {
                    skipItems(1) // Loading state
                    advanceTimeBy(NETWORK_LATENCY + 1L)

                    // Then success with data is returned
                    val addressResource = awaitItem()
                    assertIs<Resource.Success<List<LocalAddress>>>(addressResource)
                    assertEquals(address, addressResource.data)
                }
            }
        }


        @Test
        fun onUseCaseFailure_returnsFailure() = runTest {
            turbineScope {
                // When an address viewmodel is created
                // And use case returns failure
                coEvery { getAddressUseCase.invoke() } returns flow {
                    emit(Resource.Loading())
                    delay(NETWORK_LATENCY)
                    emit(Resource.Failure(address))
                }
                viewModel = AddressViewModel(
                    getLocationUseCase,
                    getAddressUseCase,
                    geocodeAddressUseCase,
                    reverseGeocodeLocationUseCase,
                    saveAddressUseCase
                )
                viewModel.addresses.test {
                    skipItems(1) // Loading state
                    advanceTimeBy(NETWORK_LATENCY + 1L)
                    val addressResource = awaitItem()

                    // Then failure is returned
                    assertIs<Resource.Failure<List<LocalAddress>>>(addressResource)
                }
            }
        }

    }

}