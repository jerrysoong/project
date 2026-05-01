package ph.edu.cksc.college.appdev.appdev2025.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicToolbar(@StringRes title: Int) {
    TopAppBar(title = { Text(stringResource(title)) }/*, backgroundColor = toolbarColor()*/)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionToolbar(
    modifier: Modifier,
    @StringRes title: Int,
    @DrawableRes primaryActionIcon: Int,
    primaryAction: () -> Unit,
    @DrawableRes secondaryActionIcon: Int? = null,
    secondaryAction: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        //backgroundColor = toolbarColor(),
        actions = {
            Box(modifier) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                ) {
                    IconButton(onClick = primaryAction) {
                        Icon(painter = painterResource(primaryActionIcon), contentDescription = "Primary Action")
                    }
                    if (secondaryAction != null && secondaryActionIcon != null) {
                        IconButton(onClick = secondaryAction) {
                            Icon(painter = painterResource(secondaryActionIcon), contentDescription = "Secondary Action")
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if (darkTheme) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant
}