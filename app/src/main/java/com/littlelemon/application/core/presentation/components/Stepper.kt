package com.littlelemon.application.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.dimens
import com.littlelemon.application.core.presentation.designsystem.typeStyle

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
            MaterialTheme.dimens.sizeSM
        )
    ) {
        if (value > 0) {
            DestructiveIconButton(
                R.drawable.ic_minus,
                onClick = onDecrease,
                showBackground = false,
                iconDescription = stringResource(R.string.desc_decrease_quantity)
            )
            Text(
                value.toString(),
                style = MaterialTheme.typeStyle.displayMedium,
                color = MaterialTheme.colors.contentSecondary,
                modifier = Modifier.padding(end = MaterialTheme.dimens.sizeSM)
            )
        }
        SecondaryIconButton(R.drawable.ic_plus, onIncrease, iconDescription = stringResource(R.string.desc_increase_quantity))
    }
}

@Preview(showBackground = true)
@Composable
private fun StepperPreview() {
    LittleLemonTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Stepper(0, {}, {})
            Stepper(5, {}, {})
        }
    }
}