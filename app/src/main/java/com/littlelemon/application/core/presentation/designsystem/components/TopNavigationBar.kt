package com.littlelemon.application.core.presentation.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.typeStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    modifier: Modifier = Modifier,
    label: String? = null,
    @DrawableRes navigationIcon: Int? = null,
    navigationIconDescription: String? = null,
    @DrawableRes actionIcon: Int? = null,
    actionIconDescription: String? = null,
    onNavigate: () -> Unit = {},
    onAction: () -> Unit = {}
) {

    TopAppBar(
        navigationIcon = {
            navigationIcon?.let { icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = navigationIconDescription,
                    modifier = Modifier.minimumInteractiveComponentSize()
                )
            }
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                label?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typeStyle.labelMedium.copy(textAlign = TextAlign.Center),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigate() }
                    )
                }
            }
        },
        actions = {
            actionIcon?.let { icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = actionIconDescription,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clickable { onAction() }
                )
            }
        }

    )
//    Row(
//        modifier = modifier.background(MaterialTheme.colors.primary),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(
//            MaterialTheme.dimens.spacingLG
//        )
//    ) {
//        Box(
//            modifier = Modifier
//                .size(48.dp)
//                .minimumInteractiveComponentSize()
//                .clickable { onLeftIconClick() }, contentAlignment = Alignment.Center
//        ) {
//            leftIcon?.let { icon ->
//                Icon(
//                    painter = painterResource(icon),
//                    contentDescription = leftIconDescription,
//                )
//            }
//        }
//
//        Box(modifier = Modifier.fillMaxWidth()) {
//            label?.let {
//                Text(
//                    text = it,
//                    style = MaterialTheme.typeStyle.labelMedium.copy(textAlign = TextAlign.Center),
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .size(48.dp)
//                .minimumInteractiveComponentSize()
//                .clickable { onRightIconClick() }, contentAlignment = Alignment.Center
//        ) {
//            rightIcon?.let { icon ->
//                Icon(
//                    painter = painterResource(icon),
//                    contentDescription = rightIconDescription,
//                )
//            }
//        }
//    }
}

@Preview(showBackground = true)
@Composable
private fun TopNavigationBarPreview() {
    LittleLemonTheme {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TopNavigationBar(
                navigationIcon = R.drawable.ic_x,
                label = "Test Label",
                actionIcon = R.drawable.ic_check,
                modifier = Modifier.width(320.dp)
            )
            TopNavigationBar(
                navigationIcon = R.drawable.ic_x, label = "Test Label",
                modifier = Modifier.width(320.dp)
            )
            TopNavigationBar(
                navigationIcon = R.drawable.ic_x,
                modifier = Modifier.width(320.dp)
            )
        }
    }
}