package com.littlelemon.application.menu.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.DotSeparator
import com.littlelemon.application.core.presentation.components.Stepper
import com.littlelemon.application.core.presentation.components.Tag
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
import kotlin.math.min
import kotlin.time.Clock


@Composable
fun MenuCard(
    dish: Dish,
    modifier: Modifier = Modifier
) {

    // TODO: Replace with state from cart/order
    var itemCount by remember { mutableIntStateOf(1) }
    val incrementCount = { itemCount += 1 }
    val decrementCount = { itemCount = min(0, itemCount + 1) }
    // END TODO:

    val cardShape = MaterialTheme.shapes.medium
    val cardShadow = MaterialTheme.shadows.dropMD
    val screenDensity = LocalDensity.current.density

    var stepperSize by remember { mutableStateOf(Size.Zero) }

    val stepperShape = MaterialTheme.shapes.small
    val stepperCornerRadius =
        CornerRadius(stepperShape.topStart.toPx(stepperSize, LocalDensity.current))
    val cardCornerRadius =
        CornerRadius(cardShape.topStart.toPx(stepperSize, LocalDensity.current))

    Column(
        modifier = modifier
            .dropShadow(cardShape, cardShadow.firstShadow.toComposeShadow(screenDensity))
            .dropShadow(
                cardShape,
                cardShadow.secondShadow?.toComposeShadow(screenDensity) ?: Shadow(0.dp)
            )
            .background(
                MaterialTheme.colors.primary,
                shape = cardShape
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 240.dp)
                .fillMaxHeight()
                .background(Color.Green, shape = MaterialTheme.shapes.medium)
                .drawWithCache {
                    val width = size.width
                    val height = size.height
                    onDrawBehind {
//                        clipRect(
//                            left = width - stepperSize.width,
//                            right = stepperSize.width,
//                            top = 0f,
//                            bottom = stepperSize.height
//                        ) {
//                            drawRoundRect(Color.Red)
//                        }
                        val path1 = Path()
                        path1.addRoundRect(
                            RoundRect(
                                left = 0f,
                                right = width,
                                top = 0f,
                                bottom = height,
//                                cornerRadius = cardCornerRadius
                            )
                        )
                        val path2 = Path()
                        path2.addRoundRect(
                            RoundRect(
                                left = width - stepperSize.width,
                                right = width + 100f,
                                top = -100f,
                                bottom = stepperSize.height,
                            )
                        )
                        drawPath(path2, Color.Yellow)
                        val path = Path.combine(PathOperation.Difference, path1, path2)
                        drawPath(
                            path = path,
                            color = Color.Magenta,
                            style = Stroke(
                                width = 5f,
                                pathEffect = PathEffect.cornerPathEffect(64f)
                            )
                        )
//                        drawRoundRect(
//                            Color.Blue,
//                            size = stepperSize,
//                            topLeft = Offset(x = width - stepperSize.width, y = 0f),
//                            cornerRadius = stepperCornerRadius
//                        )
                    }
                },
            contentAlignment = Alignment.TopEnd
        ) {
            Stepper(
                itemCount,
                onIncrease = incrementCount,
                onDecrease = decrementCount,
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
                    MaterialTheme.colors.primary,
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
                    color = MaterialTheme.colors.contentSecondary,
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
                    Text(
                        stringResource(R.string.currency_symbol),
                        style = MaterialTheme.typeStyle.displaySmall.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 28.sp // TODO: Refactor to text style
                        ),
                        color = MaterialTheme.colors.contentAccentSecondary
                    )
                    Text(
                        stringResource(R.string.price_format, dish.price),
                        style = MaterialTheme.typeStyle.displaySmall,
                        color = MaterialTheme.colors.contentAccentSecondary
                    )
                }
                //TODO: Make DiscountedPrice Optional
                dish.discountedPrice?.let { discount ->
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
                            stringResource(R.string.price_format, dish.discountedPrice),
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
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            MenuCard(
                Dish(
                    title = "Grilled Whole Fish",
                    description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with  extra-virgin olive oil (EVOO), the primary source of monounsaturated  fats in this diet",
                    price = 29.85,
                    imageURL = "NO URL",
                    stock = 15,
                    nutritionInfo = NutritionInfo(155, 22, 15, 9),
                    discountedPrice = 15.83,
                    category = listOf(),
                    dateAdded = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    popularityIndex = 392
                )
            )
            MenuCard(
                Dish(
                    title = "Grilled Whole Fish",
                    description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with  extra-virgin olive oil (EVOO), the primary source of monounsaturated  fats in this diet",
                    price = 29.85,
                    imageURL = "NO URL",
                    stock = 15,
                    nutritionInfo = NutritionInfo(155, 22, 15, 9),
                    discountedPrice = 15.83,
                    category = listOf(),
                    dateAdded = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    popularityIndex = 392
                )
            )
            MenuCard(
                Dish(
                    title = "Grilled Whole Fish",
                    description = "The warm bread is rubbed with raw garlic cloves known for immune-boosting  and anti-inflammatory properties and generously drizzled with  extra-virgin olive oil (EVOO), the primary source of monounsaturated  fats in this diet",
                    price = 29.85,
                    imageURL = "NO URL",
                    stock = 15,
                    nutritionInfo = NutritionInfo(155, 22, 15, 9),
                    discountedPrice = 15.83,
                    category = listOf(),
                    dateAdded = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    popularityIndex = 392
                )
            )
        }
    }
}