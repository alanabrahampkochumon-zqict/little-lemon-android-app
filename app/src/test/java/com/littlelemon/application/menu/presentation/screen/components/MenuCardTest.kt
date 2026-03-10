package com.littlelemon.application.menu.presentation.screen.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.littlelemon.application.R
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.time.Clock


@RunWith(RobolectricTestRunner::class)
class MenuCardTest {


    @get:Rule
    val composeTestRule = createComposeRule()
    private val application = RuntimeEnvironment.getApplication()

    private val dish = Dish(
        title = "Grilled Whole Fish",
        description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with  extra-virgin olive oil (EVOO), the primary source of monounsaturated  fats in this diet",
        price = 29.85,
        imageURL = "https://images.pexels.com/photos/18698241/pexels-photo-18698241.jpeg",
        stock = 15,
        nutritionInfo = NutritionInfo(155, 22, 15, 9),
        discountedPrice = 15.83,
        category = listOf(),
        dateAdded = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        popularityIndex = 392
    )

    @Test
    fun menuCard_titleIsDisplayed() = runTest {
        // Given a menu card
        composeTestRule.setContent {
            MenuCard(dish)
        }

        // Then, title is displayed
        composeTestRule.onNodeWithText(dish.title).assertIsDisplayed()
    }


    @Test
    fun menuCard_descriptionIsDisplayed() = runTest {
        // Given a menu card
        composeTestRule.setContent {
            MenuCard(dish)
        }

        // Then, description is displayed
        composeTestRule.onNodeWithText(dish.description!!).assertIsDisplayed()
    }

    @Test
    fun menuCard_priceIsDisplayed() = runTest {
        // Given a menu card
        composeTestRule.setContent {
            MenuCard(dish)
        }

        // Then, price is displayed
        composeTestRule.onNodeWithText(application.getString(R.string.price_format, dish.price))
            .assertIsDisplayed()
    }

    @Test
    fun menuCard_discountedPriceIsDisplayed() = runTest {
        // Given a menu card
        composeTestRule.setContent {
            MenuCard(dish)
        }

        // Then, discounted price is displayed
        composeTestRule.onNodeWithText(
            application.getString(
                R.string.price_format,
                dish.discountedPrice
            )
        )
            .assertIsDisplayed()
    }

    @Test
    fun menuCard_caloriesIsDisplayed() = runTest {
        // Given a menu card
        composeTestRule.setContent {
            MenuCard(dish)
        }

        // Then, calories is displayed
        composeTestRule.onNodeWithText(
            application.getString(
                R.string.calories,
                dish.nutritionInfo?.calories!!
            )
        )
            .assertIsDisplayed()
    }

    @Test
    fun menuCard_proteinIsDisplayed() = runTest {
        // Given a menu card
        composeTestRule.setContent {
            MenuCard(dish)
        }

        // Then, calories is displayed
        composeTestRule.onNodeWithText(
            application.getString(
                R.string.protein,
                dish.nutritionInfo?.protein!!
            )
        )
            .assertIsDisplayed()
    }

    @Test
    fun menuCard_carbsIsDisplayed() = runTest {
        // Given a menu card
        composeTestRule.setContent {
            MenuCard(dish)
        }

        // Then, carbs is displayed
        composeTestRule.onNodeWithText(
            application.getString(
                R.string.carbs,
                dish.nutritionInfo?.carbs!!
            )
        )
            .assertIsDisplayed()
    }

    @Test
    fun menuCard_fatsIsDisplayed() = runTest {
        // Given a menu card
        composeTestRule.setContent {
            MenuCard(dish)
        }

        // Then, fats is displayed
        composeTestRule.onNodeWithText(
            application.getString(
                R.string.fats,
                dish.nutritionInfo?.fats!!
            )
        )
            .assertIsDisplayed()
    }


}