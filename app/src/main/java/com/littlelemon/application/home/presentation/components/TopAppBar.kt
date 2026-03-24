package com.littlelemon.application.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.PrimaryIconButton
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.shadows
import com.littlelemon.application.core.presentation.utils.toComposeShadow
import com.littlelemon.application.home.HomeTestTags

@Composable
fun TopAppBar(onSearchClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape =
        MaterialTheme.shapes.large.copy(topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape,
                MaterialTheme.shadows.dropSM.firstShadow.toComposeShadow(LocalDensity.current.density)
            )
            .background(MaterialTheme.colors.primary, shape)
            .padding(
                start = MaterialTheme.dimens.sizeXL,
                end = MaterialTheme.dimens.sizeXL,
                bottom = MaterialTheme.dimens.sizeXL
            )
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painterResource(R.drawable.logo_full), null, modifier = Modifier
                .height(40.dp)
                .testTag(
                    HomeTestTags.LOGO
                )
        )
        Spacer(Modifier.height(MaterialTheme.dimens.sizeLG))
        Row {
            Text("TODO: Replace with address view comp")
            Spacer(Modifier.width(MaterialTheme.dimens.sizeMD))
            PrimaryIconButton(
                R.drawable.ic_search,
                onClick = onSearchClick,
                modifier
                    .size(56.dp)
                    .testTag(HomeTestTags.SEARCH_BUTTON)
            )
        }
    }
}


@Preview
@Composable
private fun TopAppBarPreview() {
    LittleLemonTheme {
        TopAppBar({})
    }
}