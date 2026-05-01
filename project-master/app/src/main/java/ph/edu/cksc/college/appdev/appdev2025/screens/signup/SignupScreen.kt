package ph.edu.cksc.college.appdev.appdev2025.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ph.edu.cksc.college.appdev.appdev2025.common.composable.BasicButton
import ph.edu.cksc.college.appdev.appdev2025.common.composable.BasicToolbar
import ph.edu.cksc.college.appdev.appdev2025.common.composable.EmailField
import ph.edu.cksc.college.appdev.appdev2025.common.composable.PasswordField
import ph.edu.cksc.college.appdev.appdev2025.common.composable.RepeatPasswordField
import ph.edu.cksc.college.appdev.appdev2025.common.ext.basicButton
import ph.edu.cksc.college.appdev.appdev2025.common.ext.fieldModifier
import ph.edu.cksc.college.appdev.appdev2025.ui.theme.AppDev2025Theme
import ph.edu.cksc.college.appdev.appdev2025.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign Up") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Signup Screen Content Goes Here")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    navController.navigate("LoginScreen")
                }) {
                    Text("Go to Login")
                }
                Spacer(modifier = Modifier.height(16.dp))
                SignUpScreen(
                    openAndPopUp = { route, popUp ->
                        navController.navigate(route) {
                            popUpTo(popUp) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
) {
    // Local UI state instead of ViewModel + Hilt
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    // Replace your ViewModel logic here if needed
    fun onSignUpClick() {
        // Add your sign-up validation/logic here
        openAndPopUp("NextScreen", "SignUpScreen")
    }

    SignUpScreenContent(
        uiState = SignUpUiState(email, password, repeatPassword),
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onRepeatPasswordChange = { repeatPassword = it },
        onSignUpClick = ::onSignUpClick
    )
}

@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit
) {
    val fieldModifier = Modifier.fieldModifier()

    BasicToolbar(AppText.generate_account)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(uiState.email, onEmailChange, fieldModifier)
        PasswordField(uiState.password, onPasswordChange, fieldModifier)
        RepeatPasswordField(uiState.repeatPassword, onRepeatPasswordChange, fieldModifier)

        BasicButton(AppText.generate_account, Modifier.basicButton()) {
            onSignUpClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val uiState = SignUpUiState(
        email = "email@test.com",
        password = "",
        repeatPassword = ""
    )

    AppDev2025Theme {
        SignUpScreenContent(
            uiState = uiState,
            onEmailChange = { },
            onPasswordChange = { },
            onRepeatPasswordChange = { },
            onSignUpClick = { }
        )
    }
}