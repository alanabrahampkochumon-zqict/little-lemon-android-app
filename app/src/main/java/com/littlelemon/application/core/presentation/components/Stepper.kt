package com.littlelemon.application.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.CoreTestTags
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun Stepper(
    value: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            LittleLemonTheme.dimens.sizeSM
        )
    ) {
        AnimatedVisibility(
            value > 0, enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DestructiveIconButton(
                    R.drawable.ic_minus,
                    onClick = onDecrease,
                    showBackground = false,
                    iconDescription = stringResource(R.string.desc_decrease_quantity),
                    modifier = Modifier.testTag(
                        CoreTestTags.STEPPER_DECREASE
                    )
                )
                AnimatedContent(targetState = value, transitionSpec = {
                    if (targetState > initialState) {
                        (slideInVertically { it } + fadeIn()) togetherWith slideOutVertically { -it } + fadeOut()
                    } else {
                        (slideInVertically { -it } + fadeIn()) togetherWith slideOutVertically { it } + fadeOut()
                    }.using(SizeTransform(clip = false))
                }) { targetValue ->
                    Text(
                        targetValue.toString(),
                        style = LittleLemonTheme.typography.displayMedium,
                        color = LittleLemonTheme.colors.contentSecondary,
                        modifier = Modifier.padding(end = LittleLemonTheme.dimens.sizeSM)
                    )
                }
            }
        }
        SecondaryIconButton(
            R.drawable.ic_plus,
            onIncrease,
            iconDescription = stringResource(R.string.desc_increase_quantity),
            modifier = Modifier.testTag(
                CoreTestTags.STEPPER_INCREASE
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StepperPreview() {
    val step = remember { mutableIntStateOf(5) }
    LittleLemonTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Stepper(0, {}, {})
            Stepper(step.intValue, { step.intValue-- }, { step.intValue++ })
            Stepper(5, {}, {})
        }
    }
}