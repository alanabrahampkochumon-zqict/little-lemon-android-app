package com.littlelemon.application.address.presentation

import app.cash.turbine.test
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.domain.usecase.GetLocationUseCase
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

    private lateinit var viewModel: AddressViewModel

    @BeforeEach
    fun setUp() {
        getLocationUseCase = mockk()
        saveAddressUseCase = mockk()
        getAddressUseCase = mockk()

        viewModel = AddressViewModel(
            getLocationUseCase,
            getAddressUseCase,
            saveAddressUseCase
        )
    }

    @Nested
    inner class StateChangeTests {

        @Test
        fun onLabelChange_toValidLabel_updatesTheState() = runTest {
            // Arrange
            val label = "new label"

            // Act
            viewModel.onAction(AddressActions.ChangeLabel(label))

            // Assert
            viewModel.state.test {
                val state = awaitItem()
                assertEquals(label, state.label)
            }
        }

        @Test
        fun onAddressChange_toValidAddress_updatesTheStateAndResetsAnyError() = runTest {
            // Arrange
            val address = "new address"

            // Act
            viewModel.onAction(AddressActions.ChangeAddress(address))

            // Assert
            viewModel.state.test {
                val state = awaitItem()
                assertEquals(address, state.address)
                assertNull(state.addressError)
            }
        }

        @Test
        fun onCityChange_toValidCity_updatesTheStateAndResetsAnyError() = runTest {
            // Arrange
            val city = "city"

            // Act
            viewModel.onAction(AddressActions.ChangeCity(city))

            // Assert
            viewModel.state.test {
                val state = awaitItem()
                assertEquals(city, state.city)
                assertNull(state.cityError)
            }
        }

        @Test
        fun onStreetAddressChange_toValidStreetAddress_updatesTheStateAndResetsAnyError() =
            runTest {
                // Arrange
                val streetAddress = "street address"

                // Act
                viewModel.onAction(AddressActions.ChangeStreetAddress(streetAddress))

                // Assert
                viewModel.state.test {
                    val state = awaitItem()
                    assertEquals(streetAddress, state.streetAddress)
                    assertNull(state.streetAddressError)
                }
            }

        @Test
        fun onStateChange_toValidState_updatesTheStateAndResetsAnyError() = runTest {
            // Arrange
            val stateName = "state"

            // Act
            viewModel.onAction(AddressActions.ChangeState(stateName))

            // Assert
            viewModel.state.test {
                val state = awaitItem()
                assertEquals(stateName, state.state)
                assertNull(state.stateError)
            }
        }

        @Test
        fun onPinCodeChange_toValidPinCode_updatesTheStateAndResetsAnyError() = runTest {
            // Arrange
            val pinCode = "pinCode"

            // Act
            viewModel.onAction(AddressActions.ChangePinCode(pinCode))

            // Assert
            viewModel.state.test {
                val state = awaitItem()
                assertEquals(pinCode, state.pinCode)
                assertNull(state.pinCodeError)
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
        fun onRequestLocation_success_showsInfoAndTriggersAddressSavedEvent() = runTest {
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
                assertIs<AddressEvents.AddressSaved>(awaitItem())
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
    inner class SaveLocationTests {

        @Test
        fun onSaveAddress_success_showInfoAndTriggersAddressSavedEvent() = runTest {
            // Arrange
            coEvery { saveAddressUseCase.invoke(any()) } returns Resource.Success()

            viewModel.addressEvents.test {
                // Act
                viewModel.onAction(AddressActions.SaveAddress)

                // Assert I
                val firstEvent = awaitItem()
                assertIs<AddressEvents.ShowInfo>(firstEvent)

                // Assert II
                val secondEvent = awaitItem()
                assertIs<AddressEvents.AddressSaved>(secondEvent)
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun onSaveAddress_failure_showError() = runTest {
            // Arrange
            coEvery { saveAddressUseCase.invoke(any()) } returns Resource.Failure(errorMessage = ERROR_MESSAGE)

            viewModel.addressEvents.test {
                // Act
                viewModel.onAction(AddressActions.SaveAddress)

                // Assert I
                val event = awaitItem()
                assertIs<AddressEvents.ShowError>(event)
                assertEquals(EXPECTED_ERROR, event.errorMessage)
                cancelAndConsumeRemainingEvents()
            }
        }

        @Test
        fun onSaveAddress_whileSaving_showsLoader() = runTest {
            // Arrange
            coEvery { saveAddressUseCase.invoke(any()) } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success()
            }

            viewModel.state.test {
                assertFalse(awaitItem().isLoading) // Initial Loading State

                // Act
                viewModel.onAction(AddressActions.SaveAddress)

                // Assert I
                val loadingState = awaitItem()
                assertTrue(loadingState.isLoading)

                // Assert II
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
    }
}