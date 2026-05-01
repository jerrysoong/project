package ph.edu.cksc.college.appdev.appdev2025.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import ph.edu.cksc.college.appdev.appdev2025.common.composable.BasicButton
import ph.edu.cksc.college.appdev.appdev2025.common.ext.basicButton
import ph.edu.cksc.college.appdev.appdev2025.ui.theme.AppDev2025Theme
import ph.edu.cksc.college.appdev.appdev2025.R.string as AppText

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(openAndPopUp: (String, String) -> Unit) {
    var showError by remember { mutableStateOf(false) }

    SplashScreenContent(
        onAppStart = {
            val startupSuccessful = true
            if (startupSuccessful) {
                openAndPopUp("settings", "splash")
            } else {
                showError = true
            }
        },
        shouldShowError = showError
    )
}

@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    onAppStart: () -> Unit,
    shouldShowError: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (shouldShowError) {
            Text(text = stringResource(AppText.generic_mistake))
            BasicButton(AppText.retry_again, Modifier.basicButton()) {
                onAppStart()
            }
        } else {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
        }
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        onAppStart()
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    AppDev2025Theme {
        SplashScreenContent(
            onAppStart = { },
            shouldShowError = true
        )
    }
}