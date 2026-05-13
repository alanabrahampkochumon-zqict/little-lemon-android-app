package com.littlelemon.application.profile.presentation

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.littlelemon.application.address.domain.usecase.GetAddressUseCase
import com.littlelemon.application.address.domain.usecase.RemoveAddressUseCase
import com.littlelemon.application.address.utils.AddressGenerator
import com.littlelemon.application.core.domain.utils.Resource
import com.littlelemon.application.profile.domain.ProfileRepository
import com.littlelemon.application.profile.domain.data.UserProfile
import com.littlelemon.application.utils.StandardTestDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@ExtendWith(StandardTestDispatcherRule::class)
class ProfileViewModelTest {


    private lateinit var profileRepository: ProfileRepository
    private lateinit var getAddressUseCase: GetAddressUseCase
    private lateinit var removeAddressUseCase: RemoveAddressUseCase

    private lateinit var viewModel: ProfileViewModel

    private val userProfile = UserProfile("fake name", "fake email")
    private val address = List(3) { AddressGenerator.generateLocalAddress() }

    @BeforeEach
    fun setUp() {
        profileRepository = mockk()
        getAddressUseCase = mockk()
        removeAddressUseCase = mockk()

        coEvery { profileRepository.getUserProfile() } returns userProfile
        coEvery { getAddressUseCase.invoke() } returns flow { emit(Resource.Success(address)) }
        coEvery { removeAddressUseCase.invoke(any()) } returns Unit

        viewModel = ProfileViewModel(profileRepository, getAddressUseCase, removeAddressUseCase)
    }


    @Nested
    inner class ProfileState {

        @Test
        fun isLoadingInitially() = runTest {
            turbineScope {
                viewModel =
                    ProfileViewModel(profileRepository, getAddressUseCase, removeAddressUseCase)
                viewModel.state.test {
                    assertTrue { awaitItem().isLoading }
                }
            }
        }


        @Test
        fun setsNameAndEmailAndSetsLoadingToFalseAfterInitialLoad() = runTest {
            turbineScope {
                viewModel =
                    ProfileViewModel(profileRepository, getAddressUseCase, removeAddressUseCase)
                viewModel.state.test {
                    skipItems(1)

                    val profile = awaitItem()
                    assertEquals(userProfile.email, profile.email)
                    assertEquals(userProfile.name, profile.userName)
                    assertFalse { profile.isLoading }
                }
            }
        }
    }


    @Nested
    inner class ProfileAddressState {


        @Test
        fun loadingIsTrueInitially() = runTest {
            turbineScope {
                viewModel =
                    ProfileViewModel(profileRepository, getAddressUseCase, removeAddressUseCase)
                viewModel.addresses.test {
                    assertTrue { awaitItem().addressLoading }
                }
            }
        }

        @Test
        fun onUseCaseSuccess_returnsSuccessWithResults() = runTest {
            turbineScope {
                viewModel =
                    ProfileViewModel(profileRepository, getAddressUseCase, removeAddressUseCase)
                viewModel.addresses.test {
                    skipItems(1) // Loading state

                    // Then success with data is returned
                    val addressResource = awaitItem()
                    assertEquals(address, addressResource.address)
                    assertNull(addressResource.addressError)
                    assertFalse(addressResource.addressLoading)
                }
            }
        }


        @Test
        fun onUseCaseFailure_setAddressFailureToNonNull() = runTest {
            turbineScope {
                coEvery { getAddressUseCase.invoke() } returns flow {
                    emit(Resource.Loading())
                    emit(Resource.Failure(address))
                }

                viewModel =
                    ProfileViewModel(profileRepository, getAddressUseCase, removeAddressUseCase)
                viewModel.addresses.test {
                    skipItems(1) // Loading state
                    val addressResource = awaitItem()

                    // Then failure is returned
                    assertNotNull(addressResource.addressError)
                }
            }
        }
    }


    @Nested
    inner class Actions {

        @Nested
        inner class RemoveAddress {

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun removeAddressInvokesUseCase() = runTest(UnconfinedTestDispatcher()) {
                turbineScope {
                    viewModel =
                        ProfileViewModel(
                            profileRepository, getAddressUseCase, removeAddressUseCase
                        )
                    viewModel.onAction(ProfileActions.RemoveAddress(address[0]))
                    coVerify { removeAddressUseCase.invoke(any()) }
                }
            }
        }
    }
}