package com.littlelemon.application.core.presentation.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import com.littlelemon.application.core.presentation.designsystem.colors
import com.littlelemon.application.core.presentation.designsystem.shadowElevation
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
    onAction: () -> Unit = {},
    scrollBehaviour: TopAppBarScrollBehavior? = null,
) {

    TopAppBar(
        modifier = modifier.shadow(MaterialTheme.shadowElevation.small),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colors.primary,
            titleContentColor = MaterialTheme.colors.contentPrimary,
            actionIconContentColor = MaterialTheme.colors.contentPrimary
        ),
        scrollBehavior = scrollBehaviour,
        navigationIcon = {
            navigationIcon?.let { icon ->
                Box(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .fillMaxHeight()
                        .clickable { onNavigate() }
                        .clickable { onNavigate() }, contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = navigationIconDescription,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                    )
                }
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
                    )
                }
            }
        },
        actions = {
            actionIcon?.let { icon ->
                Box(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .fillMaxHeight()
                        .clickable { onAction() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = actionIconDescription,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                    )
                }
            }
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
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