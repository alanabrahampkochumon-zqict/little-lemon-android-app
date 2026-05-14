package com.littlelemon.application.orders.presentation.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.littlelemon.application.R
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class NoAddressCardTest {

    @get:Rule
    val testRule = createComposeRule()
    private val application = RuntimeEnvironment.getApplication()

    @Test
    fun onSelectAddress_triggersCallback() = runTest {
        var callbackTriggered = false
        testRule.setContent {
            NoAddressCard({ callbackTriggered = true })
        }

        testRule.onNodeWithText(application.getString(R.string.select_address)).performClick()

        assertTrue { callbackTriggered }
    }

}