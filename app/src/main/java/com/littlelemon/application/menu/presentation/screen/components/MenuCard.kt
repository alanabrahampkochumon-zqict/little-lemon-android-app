package com.littlelemon.application.menu.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.DotSeparator
import com.littlelemon.application.core.presentation.components.Stepper
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.utils.applyShadow
import com.littlelemon.application.shared.menu.domain.models.Dish
import com.littlelemon.application.shared.menu.domain.models.NutritionInfo
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


@Composable
fun MenuCard(
    dish: Dish,
    orderQuantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    outOfStock: Boolean = false,
) {

    val cardShape = LittleLemonTheme.shapes.md
    val screenDensity = LocalDensity.current.density
    val soldOut = outOfStock || dish.stock < 1

    var stepperSize by remember { mutableStateOf(Size.Zero) }


    val imageCornerRadius = LittleLemonTheme.dimens.sizeXL.value * screenDensity
    val imageShape = remember(stepperSize, imageCornerRadius) {
        MenuImageShape(
            stepperSize,
            cornerRadius = imageCornerRadius,
        )
    }

    val cardColor = LittleLemonTheme.colors.primary
    Column(
        modifier = modifier
            .applyShadow(imageShape, LittleLemonTheme.shadows.dropSM)
            .background(
                LittleLemonTheme.colors.transparent, shape = cardShape
            )
            .drawBehind {
                // Draw a shape behind the card to mask out the area left by image's rounding
                val height = size.height
                val width = size.width
                val topOffset =
                    stepperSize.height + imageCornerRadius // Draw from bottom of stepper offset after accounting for corner radius(which gives rough height)
                val newHeight =
                    height - (topOffset + imageCornerRadius) // Reduce height by offset, and hte bottom corner radius(which is greater than the rounding)
                drawRect(
                    cardColor, topLeft = Offset(0f, topOffset), size = Size(width, newHeight)
                )
            }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 240.dp)
                .fillMaxHeight()
                .background(
                    LittleLemonTheme.colors.transparent, shape = LittleLemonTheme.shapes.md
                ), contentAlignment = Alignment.TopEnd
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(dish.imageURL)
                    .crossfade(true).build(),
                placeholder = placeholder ?: painterResource(R.drawable.illustration_image_loading),
                error = placeholder ?: painterResource(R.drawable.illustration_image_loading),
                contentDescription = dish.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(imageShape)
                    .fillMaxSize(),
                colorFilter = if (soldOut) ColorFilter.colorMatrix(ColorMatrix().apply {
                    setToSaturation(0f)
                } // Makes out of stock images black and white
                ) else null)
            Stepper(
                orderQuantity,
                onIncrease = onIncreaseQuantity,
                onDecrease = onDecreaseQuantity,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        stepperSize = coordinates.size.toSize()
                    }
                    .background(
                        Color.Transparent, LittleLemonTheme.shapes.sm
                    )
                    .padding(
                        LittleLemonTheme.dimens.sizeSM
                    )

            )
        }
        Column(
            modifier = Modifier
                .background(
                    cardColor,
                    shape = cardShape.copy(topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp))
                )
                .padding(
                    start = LittleLemonTheme.dimens.sizeXL,
                    end = LittleLemonTheme.dimens.sizeXL,
                    top = LittleLemonTheme.dimens.sizeSM,
                    bottom = LittleLemonTheme.dimens.sizeXL
                )
        ) {

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Dish Title
                Text(
                    dish.title,
                    style = LittleLemonTheme.typography.headlineSmall,
                    color = LittleLemonTheme.colors.contentPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .alignByBaseline(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,

                    )
                // Pricing
                if (soldOut) {
                    Text(
                        stringResource(R.string.currency_symbol) + stringResource(
                            R.string.price_format, dish.price
                        ),
                        style = LittleLemonTheme.typography.bodySmall,
                        color = LittleLemonTheme.colors.contentPlaceholder,
                        textDecoration = TextDecoration.LineThrough,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(LittleLemonTheme.dimens.sizeSM))
                    Text(
                        stringResource(
                            R.string.out_of_stock, dish.discountedPrice ?: dish.price
                        ),
                        style = LittleLemonTheme.typography.headlineSmall,
                        color = LittleLemonTheme.colors.contentDisabled,
                        modifier = Modifier
                            .padding(vertical = LittleLemonTheme.dimens.sizeSM)
                            .alignByBaseline()
                    )
                } else {
                    dish.discountedPrice?.let {
                        Text(
                            stringResource(R.string.currency_symbol) + stringResource(
                                R.string.price_format, dish.price
                            ),
                            style = LittleLemonTheme.typography.bodySmall,
                            color = LittleLemonTheme.colors.contentPlaceholder,
                            textDecoration = TextDecoration.LineThrough,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                    Spacer(modifier = Modifier.width(LittleLemonTheme.dimens.sizeSM))
                    Text(
                        stringResource(R.string.currency_symbol),
                        style = LittleLemonTheme.typography.displaySmall,
                        color = LittleLemonTheme.colors.contentAccentSecondary,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        stringResource(
                            R.string.price_format, dish.discountedPrice ?: dish.price
                        ),
                        style = LittleLemonTheme.typography.displayLarge,
                        color = LittleLemonTheme.colors.contentAccentSecondary,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }

            dish.description?.let {
                Text(
                    it,
                    style = LittleLemonTheme.typography.bodyMedium,
                    color = if (soldOut) LittleLemonTheme.colors.contentDisabled else LittleLemonTheme.colors.contentSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(LittleLemonTheme.dimens.sizeLG))
            dish.nutritionInfo?.let { nutrition ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        LittleLemonTheme.dimens.sizeSM
                    )
                ) {
                    Text(
                        stringResource(R.string.calories, nutrition.calories),
                        style = LittleLemonTheme.typography.bodyXSmall,
                        color = LittleLemonTheme.colors.contentTertiary
                    )
                    DotSeparator()
                    Text(
                        stringResource(R.string.protein, nutrition.protein),
                        style = LittleLemonTheme.typography.bodyXSmall,
                        color = LittleLemonTheme.colors.contentTertiary
                    )
                    DotSeparator()
                    Text(
                        stringResource(R.string.carbs, nutrition.carbs),
                        style = LittleLemonTheme.typography.bodyXSmall,
                        color = LittleLemonTheme.colors.contentTertiary
                    )
                    DotSeparator()
                    Text(
                        stringResource(R.string.fats, nutrition.fats),
                        style = LittleLemonTheme.typography.bodyXSmall,
                        color = LittleLemonTheme.colors.contentTertiary
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, heightDp = 1600)
@Composable
private fun MenuCardPreview() {
    LittleLemonTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)
        ) {
            MenuCard(
                Dish(
                    id = "",
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
                ),
                orderQuantity = 2,
                onDecreaseQuantity = {},
                onIncreaseQuantity = {},
                placeholder = painterResource(R.drawable.illustration_image_loading)
            )
            MenuCard(
                Dish(
                    id = "",
                    title = "Grilled Whole Fish",
                    description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with extra-virgin olive oil, the primary source of monounsaturated  fats in this diet",
                    price = 29.85,
                    imageURL = "https://images.pexels.com/photos/18698241/pexels-photo-18698241.jpeg",
                    stock = 15,
                    nutritionInfo = NutritionInfo(155, 22, 15, 9),
                    discountedPrice = 15.83,
                    category = listOf(),
                    dateAdded = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    popularityIndex = 392
                ),
                orderQuantity = 0,
                outOfStock = true,
                onDecreaseQuantity = {},
                onIncreaseQuantity = {},
                placeholder = painterResource(R.drawable.illustration_image_loading)
            )
            MenuCard(
                Dish(
                    id = "",
                    title = "Grilled Whole Fish",
                    description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with extra-virgin olive oil, the primary source of monounsaturated  fats in this diet",
                    price = 29.85,
                    imageURL = "https://images.pexels.com/photos/18698241/pexels-photo-18698241.jpeg",
                    stock = 15,
                    nutritionInfo = NutritionInfo(155, 22, 15, 9),
                    discountedPrice = null,
                    category = listOf(),
                    dateAdded = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    popularityIndex = 392
                ),
                orderQuantity = 0,
                onDecreaseQuantity = {},
                onIncreaseQuantity = {},
                placeholder = painterResource(R.drawable.illustration_image_loading)
            )
            MenuCard(
                Dish(
                    id = "",
                    title = "Grilled Whole Fish",
                    description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with  extra-virgin olive oil (EVOO), the primary source of monounsaturated  fats in this diet",
                    price = 29.85,
                    imageURL = "https://images.pexels.com/photos/18698241/pexels-photo-18698241.jpeg",
                    stock = 0,
                    nutritionInfo = NutritionInfo(155, 22, 15, 9),
                    discountedPrice = null,
                    category = listOf(),
                    dateAdded = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    popularityIndex = 392
                ),
                orderQuantity = 0,
                onDecreaseQuantity = {},
                onIncreaseQuantity = {},
                placeholder = painterResource(R.drawable.illustration_image_loading)
            )
        }
    }
}