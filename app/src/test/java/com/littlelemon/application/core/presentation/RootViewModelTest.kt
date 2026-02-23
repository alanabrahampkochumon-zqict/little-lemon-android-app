package com.littlelemon.application.core.presentation

import app.cash.turbine.test
import com.littlelemon.application.core.domain.AddressManager
import com.littlelemon.application.core.domain.SessionManager
import com.littlelemon.application.core.domain.model.SessionStatus
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExtendWith(StandardTestDispatcherRule::class)
class RootViewModelTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var addressManager: AddressManager
    private lateinit var viewModel: RootViewModel
    private lateinit var testFlow: MutableSharedFlow<SessionStatus>

    @BeforeEach
    fun setUp() {
        testFlow = MutableSharedFlow(replay = 1)
        sessionManager = mockk()
        addressManager = mockk()
        every { sessionManager.getCurrentSessionStatus() } returns testFlow
        viewModel = RootViewModel(sessionManager, addressManager)
    }

    @Test
    fun sessionStatus_onInitialization_emitsInitialState() = runTest {
        // Given no emission from session manager

        // Then, only the initial state is emitted
        viewModel.sessionStatus.test {
            val initialState = awaitItem()
            assertIs<SessionStatus.SessionLoading>(initialState)
        }
    }

    @Test
    fun sessionStatus_afterReceivingNewState_reEmitsTheNewState() = runTest {
        // Given session manager emits UserSession.FullyAuthenticated

        viewModel.sessionStatus.test {
            awaitItem() // Ignore the initial state

            // When a FullyAuthenticated event is emitted
            testFlow.emit(SessionStatus.FullyAuthenticated)

            // Then, FullyAuthenticated is emitted after initial state
            val updatedState = awaitItem()
            assertIs<SessionStatus.FullyAuthenticated>(updatedState)
        }
    }

    @Test
    fun sessionStatus_afterTwoNewState_reEmitsStatesCorrectly() = runTest {
        // Given session manager emits UserSession.FullyAuthenticated

        viewModel.sessionStatus.test {
            awaitItem() // Ignore the initial state

            // When a FullyAuthenticated event is emitted
            testFlow.emit(SessionStatus.FullyAuthenticated)

            // Then, FullyAuthenticated is emitted after initial state
            val secondState = awaitItem()
            assertIs<SessionStatus.FullyAuthenticated>(secondState)

            // And when Unauthenticated event is emitted
            testFlow.emit(SessionStatus.Unauthenticated)

            // Then, Unauthenticated is emitted after second state
            val thirdState = awaitItem()
            assertIs<SessionStatus.Unauthenticated>(thirdState)
        }
    }

    @Test
    fun userHasAddress_managerReturnsFalse_emitsFalse() = runTest {
        // Given the address manager returns false
        coEvery { addressManager.userHasAddress() } returns false

        viewModel.userHasAddress.test {
            // Then, the initial state emitted is null
            assertNull(awaitItem())

            // And emits correct state afterward
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun userHasAddress_managerReturnsTrue_emitsTrue() = runTest {
        // Given the address manager returns false
        coEvery { addressManager.userHasAddress() } returns true

        viewModel.userHasAddress.test {
            // Then, the initial state emitted is null
            assertNull(awaitItem())

            // And emits correct state afterward
            assertEquals(true, awaitItem())
        }
    }

}