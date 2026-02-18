package com.littlelemon.application.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle
import com.littlelemon.application.core.presentation.designsystem.xLarge
import kotlinx.coroutines.delay

@Composable
fun ResendTimer(
    modifier: Modifier = Modifier,
    initialTime: Int = 60,
    totalTime: Int = 60,
    onChangeEmail: () -> Unit = {}
) {

    var timeLeft by rememberSaveable {
        mutableIntStateOf(initialTime)
    }
    val isTimerRunning = timeLeft > 0

    LaunchedEffect(key1 = timeLeft, key2 = isTimerRunning) {
        if (isTimerRunning) {
            delay(1000L)
            timeLeft--
        }
    }

    if (isTimerRunning) {
        Box(modifier = Modifier.padding(end = MaterialTheme.dimens.size2XL)) {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { timeLeft / totalTime.toFloat() },
                    modifier = Modifier.size(
                        MaterialTheme.dimens.size3XL
                    ),
                    strokeWidth = MaterialTheme.dimens.sizeXS,
                    strokeCap = StrokeCap.Round,
                    trackColor = MaterialTheme.colors.secondary,
                    color = MaterialTheme.colors.contentHighlight
                )
                Text(
                    timeLeft.toString(),
                    style = MaterialTheme.typeStyle.labelSmall,
                    color = MaterialTheme.colors.contentTertiary
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colors.secondary,
                    shape = MaterialTheme.shapes.xLarge.copy(
                        topEnd = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    )
                )
                .padding(
                    top = MaterialTheme.dimens.sizeLG,
                    bottom = MaterialTheme.dimens.sizeLG,
                    start = MaterialTheme.dimens.size2XL,
                    end = MaterialTheme.dimens.size3XL
                )
                .clickable { onChangeEmail() }

        ) {
            Text(
                stringResource(R.string.act_resend_otp),
                style = MaterialTheme.typeStyle.labelMedium,
                color = MaterialTheme.colors.contentHighlight
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ResendTimerPreview() {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End
    ) {
        ResendTimer()
        ResendTimer(initialTime = 30)
        ResendTimer(initialTime = 0)
    }
}