package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.littlelemon.application.core.presentation.designsystem.colors

@Composable
fun DoodleBackground(modifier: Modifier = Modifier, alpha: Float = 0.15f) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colors.secondary)
    ) {
        // TODO: Implement using individual vector collection
//        Image(
//            painter = painterResource(R.drawable.doodles),
//            contentDescription = stringResource(R.string.desc_logo),
//            Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop,
//            colorFilter = ColorFilter.tint(MaterialTheme.colors.contentHighlight),
//            alpha = alpha
//        )
    }
}