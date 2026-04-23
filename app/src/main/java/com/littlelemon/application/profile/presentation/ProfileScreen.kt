package com.littlelemon.application.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.components.Header
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    // TODO: Replace
    val name = "Mitch Lebron"
    val email = "mitch@lebron.com"

    ///////
    LazyColumn(modifier) {
        // Basic information
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.Red, LittleLemonTheme.shapes.xl)
                )
                Spacer(Modifier.width(LittleLemonTheme.dimens.sizeLG))
                Column {
                    Text(name, style = LittleLemonTheme.typography.displayMedium)
                    Text(
                        email,
                        style = LittleLemonTheme.typography.bodySmall,
                        modifier = Modifier.offset(y = (-4).dp)
                    )
                }
            }
        }
        item {
            Header(stringResource(R.string.addresses))
        }
        items(5) {
//            AddressCard
        }
    }
}


@Preview
@Composable
private fun ProfileScreenPreview() {
    LittleLemonTheme {
        ProfileScreen()
    }
}