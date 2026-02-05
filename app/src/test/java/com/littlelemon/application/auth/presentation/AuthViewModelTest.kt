package com.littlelemon.application.auth.presentation

import app.cash.turbine.test
import com.littlelemon.application.auth.domain.usecase.ResendOTPUseCase
import com.littlelemon.application.auth.domain.usecase.SendOTPUseCase
import com.littlelemon.application.auth.domain.usecase.ValidateEmailUseCase
import com.littlelemon.application.auth.domain.usecase.ValidateOTPUseCase
import com.littlelemon.application.auth.domain.usecase.VerifyOTPUseCase
import com.littlelemon.application.auth.domain.usecase.params.VerificationParams
import com.littlelemon.application.auth.presentation.components.AuthActions
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.core.domain.utils.ValidationError
import com.littlelemon.application.core.domain.utils.ValidationResult
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(StandardTestDispatcherRule::class)
class AuthViewModelTest {
    companion object {
        private const val DEBOUNCE_DELAY_MS = 1000L
        private const val NETWORK_LATENCY = 500L
    }

    private val validateEmailUseCase = mockk<ValidateEmailUseCase>(relaxed = true)
    private val validateOTPUseCase = mockk<ValidateOTPUseCase>()
    private val sendOTPUseCase = mockk<SendOTPUseCase>()
    private val verifyOTPUseCase = mockk<VerifyOTPUseCase>()
    private val resendOTPUseCase = mockk<ResendOTPUseCase>()
    private lateinit var viewModel: AuthViewModel

    @BeforeEach
    fun setUp() {
        viewModel =
            AuthViewModel(
                validateEmailUseCase,
                validateOTPUseCase,
                sendOTPUseCase,
                verifyOTPUseCase,
                resendOTPUseCase
            )
    }

    @Nested
    inner class EmailValidationTests {

        @Test
        fun onEmailChange_toIncompleteValue_stateUpdatesEmailWithoutErrorMessage() = runTest {
            // Arrange
            val email = "test"
            val error = ValidationError.InvalidFormat
            val message = "invalid email"
            coEvery { validateEmailUseCase.invoke(email) } returns ValidationResult.Failure(
                error, message
            )

            // Act
            viewModel.onAction(AuthActions.ChangeEmail(email = email))

            // Assert that there are no error messages, value is updated, and email is updated.
            assertEquals(email, viewModel.state.value.email)
            assertFalse(viewModel.state.value.enableSendButton)
            assertNull(viewModel.state.value.emailError)
        }

        @Test
        fun onEmailChange_toValidEmail_stateUpdatesToNewEmailAndEnableSendButton() = runTest {
            // Arrange
            val email = "test@gmail.com"
            coEvery { validateEmailUseCase.invoke(email) } returns ValidationResult.Success

            // Act
            viewModel.onAction(AuthActions.ChangeEmail(email = email))

            // Assert
            assertEquals(email, viewModel.state.value.email)
            assertTrue(viewModel.state.value.enableSendButton)
            assertNull(viewModel.state.value.emailError)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onEmailChange_toInvalidEmail_stateUpdatesErrorMessageWithDelayAndDisablesSendButton() =
            runTest {
                // Arrange
                val email = "invalid_email.com"
                val error = ValidationError.InvalidFormat
                val message = "invalid email"
                coEvery { validateEmailUseCase.invoke(email) } returns ValidationResult.Failure(
                    error, message
                )
                viewModel.state.test {
                    skipItems(1)

                    // Act
                    viewModel.onAction(AuthActions.ChangeEmail(email = email))

                    // Assert
                    val firstState = awaitItem()
                    assertEquals(email, firstState.email)
                    assertFalse(firstState.enableSendButton)
                    assertNull(firstState.emailError)

                    advanceTimeBy(DEBOUNCE_DELAY_MS + 1)

                    // Assert II
                    val delayedState = awaitItem()
                    assertNotNull(delayedState.emailError)
                }
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onEmailChange_afterInvalidEmailEntered_clearsErrorMessage() = runTest {
            // Arrange
            val email = "invalid_email.com"
            val updatedEmail = "test"

            val error = ValidationError.InvalidFormat
            val message = "invalid email"
            coEvery { validateEmailUseCase.invoke(email) } returns ValidationResult.Failure(
                error, message
            )
            coEvery { validateEmailUseCase.invoke(updatedEmail) } returns ValidationResult.Failure(
                error,
                message
            )

            viewModel.state.test {
                skipItems(1)

                // Act
                viewModel.onAction(AuthActions.ChangeEmail(email = email))

                // Assert
                val firstState = awaitItem()
                assertEquals(email, firstState.email)
                assertFalse(firstState.enableSendButton)
                assertNull(firstState.emailError)

                advanceTimeBy(DEBOUNCE_DELAY_MS)
                val debouncedState = awaitItem()

                assertNotNull(debouncedState.emailError)

                // Act II
                viewModel.onAction(AuthActions.ChangeEmail(email = updatedEmail))
                val updatedState = awaitItem()

                // Assert
                assertEquals(updatedEmail, updatedState.email)
                assertFalse(updatedState.enableSendButton)
                assertNull(updatedState.emailError)
            }


        }
    }

    @Nested
    inner class OTPValidationTests {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onOTPChange_toValidOTP_stateUpdatesToNewOTP() = runTest {
            // Arrange
            val newOTP = listOf(3, 1, 6, 3, 1, 6)
            coEvery { validateOTPUseCase.invoke(newOTP.joinToString("")) } returns ValidationResult.Success

            // Act
            viewModel.onAction(AuthActions.ChangeOTP(otp = newOTP))
            runCurrent()

            // Assert
            assertEquals(newOTP, viewModel.state.value.oneTimePassword)
            assertTrue(viewModel.state.value.enableVerifyButton)
            assertNull(viewModel.state.value.otpError)
        }

        // This test covers when user deletes an invalid otp as well.
        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onOTPChange_toIncompleteOTP_verifyButtonIsDisabled() = runTest {
            // Arrange
            val error = ValidationError.InvalidFormat
            val message = "invalid otp"
            val newOTP = listOf(3, 1, 6, 3, 1)
            coEvery { validateOTPUseCase.invoke(newOTP.joinToString("")) } returns ValidationResult.Failure(
                error, message
            )

            // Act
            viewModel.onAction(AuthActions.ChangeOTP(otp = newOTP))
            runCurrent()

            // Assert
            assertEquals(newOTP, viewModel.state.value.oneTimePassword)
            assertFalse(viewModel.state.value.enableVerifyButton)
            assertNull(viewModel.state.value.otpError)
        }
    }

    @Nested
    inner class NameValidationTests {

        @Test
        fun onFirstNameChange_toEmptyString_buttonIsDisabled() = runTest {
            // Arrange
            val firstName = ""

            // Act
            viewModel.onAction(AuthActions.ChangeFirstName(firstName = firstName))

            // Assert
            assertEquals(firstName, viewModel.state.value.firstName)
            assertFalse(viewModel.state.value.enableLetsGoButton)
        }

        @Test
        fun onFirstNameChange_toValidFirstName_stateUpdatesToNewFirstName() = runTest {
            // Arrange
            val firstName = "First Name"

            // Act
            viewModel.onAction(AuthActions.ChangeFirstName(firstName = firstName))

            // Assert
            assertEquals(firstName, viewModel.state.value.firstName)
            assertTrue(viewModel.state.value.enableLetsGoButton)
        }


        @Test
        fun onLastNameChangeAndValidFirstName_toValidLastName_stateUpdatesToNewLastNameWithButtonEnabled() =
            runTest {
                // Arrange
                val lastName = "Last Name"
                val firstName = "First Name"

                // Act
                viewModel.onAction(AuthActions.ChangeFirstName(firstName = firstName))
                viewModel.onAction(AuthActions.ChangeLastName(lastName = lastName))

                // Assert
                assertEquals(lastName, viewModel.state.value.lastName)
                assertTrue(viewModel.state.value.enableLetsGoButton)

            }
    }

    @Nested
    inner class NavigationTests {
        private val otp = listOf(3, 1, 6, 3, 1, 6)
        private val email = "test@test.com"

        @OptIn(ExperimentalCoroutinesApi::class)
        @BeforeEach
        fun setUp() {
            coEvery { validateOTPUseCase.invoke(otp.joinToString("")) } returns ValidationResult.Success
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onSendOTP_whileSending_loadingIsTrue() = runTest {

            viewModel.onAction(AuthActions.ChangeOTP(otp))
            runCurrent()

            coEvery { sendOTPUseCase.invoke(otp.joinToString("")) } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success()
            }

            viewModel.state.test {
                // Initial Assertion
                assertFalse(awaitItem().isLoading)

                // Act
                viewModel.onAction(AuthActions.SendOTP)
                runCurrent()

                // State transition Assert
                assertTrue(awaitItem().isLoading, "Should be loading during the delay")

                // Advancing delay
                advanceTimeBy(NETWORK_LATENCY + 1)
                runCurrent()

                // Final Assert for state reset
                assertFalse(awaitItem().isLoading)
            }

        }

        @Test
        fun onSendOTP_sendOTPSuccess_navigationIsTriggered() = runTest {
            // Arrange
            viewModel.onAction(AuthActions.ChangeOTP(otp))
            coEvery { sendOTPUseCase.invoke(otp.joinToString("")) } returns Resource.Success()

            viewModel.authEvent.test {
                // Act
                viewModel.onAction(AuthActions.SendOTP)
                val event = awaitItem()

                // Assert
                assertNotNull(event)
                assertTrue(event is AuthEvents.NavigateToOTPScreen)

            }
        }

        @Test
        fun onSendOTP_sendOTPError_errorEventIsTriggered() = runTest {
            // Arrange
            viewModel.onAction(AuthActions.ChangeOTP(otp))
            coEvery { sendOTPUseCase.invoke(otp.joinToString("")) } returns Resource.Failure()

            viewModel.authEvent.test {
                // Act
                viewModel.onAction(AuthActions.SendOTP)
                val event = awaitItem()

                // Assert
                assertNotNull(event)
                assertTrue(event is AuthEvents.ShowError)
            }
        }

        @Test
        fun onNavigateBack_navigateBackEventIsTriggered() = runTest {
            viewModel.authEvent.test {
                // Arrange & Act
                viewModel.onAction(AuthActions.NavigateBack)
                val event = awaitItem()

                // Assert
                assertTrue(event is AuthEvents.NavigateBack)
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onVerifyOTP_whileVerifying_loadingIsTrue() = runTest {
            val verificationParams = VerificationParams(email, otp.joinToString(""))
            viewModel.onAction(AuthActions.ChangeEmail(email))
            coEvery { verifyOTPUseCase.invoke(verificationParams) } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success()
            }

            viewModel.onAction(AuthActions.ChangeOTP(otp))
            runCurrent()

            viewModel.state.test {
                // Assert Loading Not Shown
                assertFalse(awaitItem().isLoading)

                // Act I
                viewModel.onAction(AuthActions.VerifyOTP)
                runCurrent()

                // Assert Loading Shown
                assertTrue(awaitItem().isLoading)

                advanceTimeBy(NETWORK_LATENCY + 1)
                val job = launch { viewModel.authEvent.collect { } } // Consume any events

                // Assert Loading is not shown after event is handled
                assertFalse(awaitItem().isLoading)
                cancelAndIgnoreRemainingEvents()
                job.cancel()
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onVerifyOTP_verificationSuccess_navigationEventIsTriggered() = runTest {
            val verificationParams = VerificationParams(email, otp.joinToString(""))
            viewModel.onAction(AuthActions.ChangeEmail(email))
            viewModel.onAction(AuthActions.ChangeOTP(otp))
            coEvery { verifyOTPUseCase.invoke(verificationParams) } returns Resource.Success()

            viewModel.authEvent.test {
                // Act
                viewModel.onAction(AuthActions.VerifyOTP)
                val event = awaitItem()

                // Assert
                assertTrue(event is AuthEvents.NavigateToPersonalizationScreen)
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onVerifyOTP_verificationFailure_showErrorEventIsTriggered() = runTest {
            val verificationParams = VerificationParams(email, otp.joinToString(""))
            viewModel.onAction(AuthActions.ChangeEmail(email))
            viewModel.onAction(AuthActions.ChangeOTP(otp))
            coEvery { verifyOTPUseCase.invoke(verificationParams) } returns Resource.Failure()

            viewModel.authEvent.test {
                // Act
                viewModel.onAction(AuthActions.VerifyOTP)
                val event = awaitItem()

                // Assert
                assertTrue(event is AuthEvents.ShowError)
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun onResendOTP_whileResending_loadingIsTrue() = runTest() {
            // Arrange
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            viewModel = AuthViewModel(
                validateEmailUseCase,
                validateOTPUseCase,
                sendOTPUseCase,
                verifyOTPUseCase,
                resendOTPUseCase
            )
            coEvery { resendOTPUseCase.invoke(any()) } coAnswers {
                delay(NETWORK_LATENCY)
                Resource.Success()
            }
//            viewModel.onAction(AuthActions.ChangeEmail(email))
//            runCurrent()

            viewModel.state.test {
                // Assert State before request
                assertFalse(awaitItem().isLoading)

                // Act: Send the otp
                viewModel.onAction(AuthActions.ResendOTP)
                val s2 = awaitItem()
                assertTrue(s2.isLoading)

                advanceTimeBy(NETWORK_LATENCY + 1)

                // Collect the triggered event. Else the coroutine will suspend forever.
                val job =
                    launch { viewModel.authEvent.collect { } }
                val s3 = awaitItem()
                assertFalse(s3.isLoading)
                job.cancel()
            }
        }

        @Test
        fun onResendOTP_resendSuccess_showInfoEventIsTriggered() = runTest {
            // Arrange
            coEvery { resendOTPUseCase.invoke(email) } returns Resource.Success()
            viewModel.onAction(AuthActions.ChangeEmail(email))

            viewModel.authEvent.test {

                // Act: Send the otp
                viewModel.onAction(AuthActions.ResendOTP)
                val triggeredEvent = awaitItem()

                // Assert
                assertTrue(triggeredEvent is AuthEvents.ShowInfo)
            }
        }

        @Test
        fun onResendOTP_resendFailures_showErrorEventIsTriggered() = runTest {
            // Arrange
            coEvery { resendOTPUseCase.invoke(email) } returns Resource.Failure()
            viewModel.onAction(AuthActions.ChangeEmail(email))

            viewModel.authEvent.test {

                // Act: Send the otp
                viewModel.onAction(AuthActions.ResendOTP)
                val triggeredEvent = awaitItem()

                // Assert
                assertTrue(triggeredEvent is AuthEvents.ShowError)
            }
        }

    }
}