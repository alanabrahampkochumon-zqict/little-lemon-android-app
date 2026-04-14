package com.littlelemon.application.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.littlelemon.application.R
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    modifier: Modifier = Modifier,
    label: String? = null,
    subText: String? = null,
    @DrawableRes navigationIcon: Int? = null,
    navigationIconDescription: String? = null,
    onNavigate: () -> Unit = {},
    scrollBehaviour: TopAppBarScrollBehavior? = null,
) {
    // TODO: Custom ripple effect
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LittleLemonTheme.colors.transparent,
            titleContentColor = LittleLemonTheme.colors.contentPrimary,
            actionIconContentColor = LittleLemonTheme.colors.contentPrimary
        ),
        scrollBehavior = scrollBehaviour,
        navigationIcon = {
            navigationIcon?.let { icon ->
                Box(modifier = Modifier
                    .clip(LittleLemonTheme.shapes.lg)
                    .clickable { onNavigate() }
                    .minimumInteractiveComponentSize()
                    .testTag(stringResource(R.string.test_tag_navigation_action_left)),
                    contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = navigationIconDescription,
                        tint = LittleLemonTheme.colors.contentPrimary,
                    )
                }
            }
        },
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                label?.let {
                    Text(
                        text = it,
                        style = LittleLemonTheme.typography.displaySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                subText?.let {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = it,
                        style = LittleLemonTheme.typography.bodyMedium,
                        color = LittleLemonTheme.colors.contentSecondary
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
            modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TopNavigationBar(
                navigationIcon = R.drawable.ic_x,
                label = "Test Label",
                modifier = Modifier.width(320.dp)
            )
            TopNavigationBar(
                navigationIcon = R.drawable.ic_x,
                label = "Test Label",
                modifier = Modifier.width(320.dp)
            )
            TopNavigationBar(
                navigationIcon = R.drawable.ic_x, modifier = Modifier.width(320.dp)
            )
            TopNavigationBar(
                label = "Test Label",
                subText = "What should we call you?",
                modifier = Modifier.width(320.dp)
            )
        }
    }
}