package com.littlelemon.application.home.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.testing.junit.testparameterinjector.TestParameter
import com.littlelemon.application.home.presentation.components.BottomNavigation
import com.littlelemon.application.home.presentation.components.NavigationOption
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestParameterInjector
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertEquals


@RunWith(RobolectricTestParameterInjector::class)
class BottomNavigationTest {

    @get:Rule
    val testRule = createComposeRule()

    private val application = RuntimeEnvironment.getApplication()

    private val navigationOptions = NavigationOption.entries

    @Test
    fun displaysOptions(@TestParameter option: NavigationOption) {
        // Given a bottom navigation
        testRule.setContent {
            BottomNavigation({})
        }

        // Then all navigation options are displayed
        testRule.onNodeWithText(application.getString(option.label)).assertIsDisplayed()
    }

    @Test
    fun displaysCorrectSelectedState(@TestParameter option: NavigationOption) {
        // Given a bottom navigation with one of the option selected
        testRule.setContent {
            BottomNavigation({}, selected = option)
        }

        // Then, that option is selected
        testRule.onNodeWithText(application.getString(option.label)).assertIsSelected()
    }


    @Test
    fun onClickTriggersWithCorrectOption(@TestParameter option: NavigationOption) {
        // Given a bottom navigation
        var triggeredOption: NavigationOption = NavigationOption.HOME
        testRule.setContent {
            BottomNavigation({ triggeredOption = option }, selected = option)
        }

        // When an option is selected
        testRule.onNodeWithText(application.getString(option.label)).performClick()

        // Then the correct item is selected
        assertEquals(option, triggeredOption)
    }


}