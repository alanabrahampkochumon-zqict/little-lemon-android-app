package com.littlelemon.application.orders.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

// TODO: Add image loading shimmer
@Composable
fun DishImageCard(
    imageURL: String,
    dishName: String,
    quantity: Int,
    modifier: Modifier = Modifier
) {
    val innerCardShape = LittleLemonTheme.shapes.xs
    Column(
        modifier = modifier
            .background(LittleLemonTheme.colors.primary, innerCardShape)
            .padding(bottom = LittleLemonTheme.dimens.size2XS)
            .clip(innerCardShape)
            .widthIn(max = 88.dp)
    ) {
        Box(
            modifier = Modifier
                .height(56.dp)
                .width(88.dp)
                .background(LittleLemonTheme.colors.tertiary, innerCardShape)
                .clip(innerCardShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.ic_gallery),
                null,
                colorFilter = ColorFilter.tint(LittleLemonTheme.colors.contentDisabled)
            )
            AsyncImage(
                model = imageURL,
                contentDescription = dishName,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = LittleLemonTheme.dimens.sizeXS,
                        horizontal = LittleLemonTheme.dimens.sizeSM
                    ),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    Modifier
                        .background(
                            LittleLemonTheme.colors.information,
                            shape = LittleLemonTheme.shapes.xl
                        )
                        .padding(
                            horizontal = LittleLemonTheme.dimens.sizeMD,
                            vertical = LittleLemonTheme.dimens.sizeXS
                        )
                ) {
                    Text(
                        "${if (quantity < 10) quantity else "9+"}",
                        style = LittleLemonTheme.typography.bodyXSmall,
                        color = LittleLemonTheme.colors.contentOnColor
                    )
                }
            }
        }
        Text(
            text = dishName,
            style = LittleLemonTheme.typography.bodyXSmall.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .padding(
                    LittleLemonTheme.dimens.size2XS
                )
                .fillMaxWidth(), maxLines = 1, overflow = TextOverflow.Clip
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DishImageCardPreview() {
    LittleLemonTheme() {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DishImageCard("url", "Dish Name", 1)
            DishImageCard("url", "Dish Name", 5)
            DishImageCard("url", "Dish Name", 10)
        }
    }
}