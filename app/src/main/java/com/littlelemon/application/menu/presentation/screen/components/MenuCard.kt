package com.littlelemon.application.menu.presentation.screen.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.DotSeparator
import com.littlelemon.application.core.presentation.components.Stepper
import com.littlelemon.application.core.presentation.components.Tag
import com.littlelemon.application.core.presentation.components.TagVariant
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.utils.toComposeShadow
import com.littlelemon.application.menu.domain.models.Dish
import com.littlelemon.application.menu.domain.models.NutritionInfo
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


// TODO: Out of stock and unavailable
@Composable
fun MenuCard(
    dish: Dish,
    orderQuantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    modifier: Modifier = Modifier,
    outOfStock: Boolean = false,
) {

    val cardShape = MaterialTheme.shapes.medium
    val cardShadow = MaterialTheme.shadows.dropMD
    val screenDensity = LocalDensity.current.density

    var stepperSize by remember { mutableStateOf(Size.Zero) }


    val imageCornerRadius = MaterialTheme.dimens.sizeXL.value * screenDensity
    val imageShape = remember(stepperSize, imageCornerRadius) {
        MenuImageShape(
            stepperSize,
            cornerRadius = imageCornerRadius,
        )
    }

    val cardColor = MaterialTheme.colors.primary

    Column(
        modifier = modifier
            .dropShadow(imageShape, cardShadow.firstShadow.toComposeShadow(screenDensity))
            .dropShadow(
                imageShape,
                cardShadow.secondShadow?.toComposeShadow(screenDensity) ?: Shadow(0.dp)
            )
            .background(
                MaterialTheme.colors.transparent,
                shape = cardShape
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
                    cardColor,
                    topLeft = Offset(0f, topOffset),
                    size = Size(width, newHeight)
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 240.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colors.transparent, shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.TopEnd
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dish.imageURL)
                    .crossfade(true).build(),
                placeholder = painterResource(R.drawable.illustration_image_loading),
                onError = {
                    Log.d("Coil", "Error $it")
                },
                contentDescription = dish.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(imageShape),
                colorFilter = if (outOfStock) ColorFilter.colorMatrix(
                    ColorMatrix().apply { setToSaturation(0f) } // Makes out of stock images black and white
                ) else null
            )
            Stepper(
                orderQuantity,
                onIncrease = onIncreaseQuantity,
                onDecrease = onDecreaseQuantity,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        stepperSize = coordinates.size.toSize()
                    }
                    .background(
                        Color.Transparent,
                        MaterialTheme.shapes.small
                    )
                    .padding(
                        MaterialTheme.dimens.sizeSM
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
                    start = MaterialTheme.dimens.sizeXL,
                    end = MaterialTheme.dimens.sizeXL,
                    top = MaterialTheme.dimens.sizeMD,
                    bottom = MaterialTheme.dimens.sizeLG
                )
        ) {

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.sizeLG)
            ) {
                Text(
                    dish.title,
                    style = MaterialTheme.typeStyle.headlineSmall,
                    color = MaterialTheme.colors.contentPrimary,
                    modifier = Modifier.weight(1f)
                )
                dish.nutritionInfo?.let { nutrition ->
                    if (outOfStock)
                        Tag(
                            stringResource(R.string.calories, nutrition.calories),
                            variant = TagVariant.NeutralFilled
                        )
                    else
                        Tag(stringResource(R.string.calories, nutrition.calories))
                }
            }

            dish.nutritionInfo?.let { nutrition ->
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeXS))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.sizeXS
                    )
                ) {
                    Text(
                        stringResource(R.string.protein, nutrition.protein),
                        style = MaterialTheme.typeStyle.bodyXSmall
                    )
                    DotSeparator()
                    Text(
                        stringResource(R.string.carbs, nutrition.carbs),
                        style = MaterialTheme.typeStyle.bodyXSmall
                    )
                    DotSeparator()
                    Text(
                        stringResource(R.string.fats, nutrition.fats),
                        style = MaterialTheme.typeStyle.bodyXSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeSM))

            dish.description?.let {
                Text(
                    it,
                    style = MaterialTheme.typeStyle.bodyMedium,
                    color = if (outOfStock) MaterialTheme.colors.contentDisabled else MaterialTheme.colors.contentSecondary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.sizeXL))
            Row(
                modifier = Modifier.border(
                    1.dp,
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colors.outlineSecondary
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.size2XS
                    ),
                    modifier = Modifier
                        .border(
                            1.dp,
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colors.outlineSecondary
                        )
                        .padding(
                            start = MaterialTheme.dimens.sizeMD,
                            end = MaterialTheme.dimens.sizeLG
                        ),
                ) {
                    // TODO: Add discount tag, Add out of stock
                    Text(
                        stringResource(R.string.currency_symbol),
                        style = MaterialTheme.typeStyle.displaySmall.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 28.sp // TODO: Refactor to text style
                        ),
                        color = MaterialTheme.colors.contentAccentSecondary
                    )
                    Text(
                        stringResource(
                            R.string.price_format,
                            dish.discountedPrice ?: dish.price
                        ), // TODO: Add test
                        style = MaterialTheme.typeStyle.displaySmall,
                        color = MaterialTheme.colors.contentAccentSecondary
                    )
                }
                //TODO: Make DiscountedPrice Optional
                dish.discountedPrice?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.dimens.sizeSM,
                                end = MaterialTheme.dimens.sizeMD,
                            ),
                    ) {
                        Text(
                            stringResource(R.string.currency_symbol),
                            style = MaterialTheme.typeStyle.bodySmall.copy(
                                fontSize = 12.sp // TODO: Refactor to text style
                            ),
                            color = MaterialTheme.colors.contentPlaceholder,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Text(
                            stringResource(R.string.price_format, dish.price),
                            style = MaterialTheme.typeStyle.bodySmall,
                            color = MaterialTheme.colors.contentPlaceholder,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MenuCardPreview() {
    LittleLemonTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            MenuCard(
                Dish(
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
                ), orderQuantity = 2,
                onDecreaseQuantity = {}, onIncreaseQuantity = {}
            )
            MenuCard(
                Dish(
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
                ), orderQuantity = 0,
                outOfStock = true,
                onDecreaseQuantity = {}, onIncreaseQuantity = {}
            )
            MenuCard(
                Dish(
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
                ), orderQuantity = 0,
                onDecreaseQuantity = {}, onIncreaseQuantity = {}
            )
        }
    }
}