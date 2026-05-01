package ph.edu.cksc.college.appdev.appdev2025.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import ph.edu.cksc.college.appdev.appdev2025.common.composable.BasicButton
import ph.edu.cksc.college.appdev.appdev2025.common.composable.BasicTextButton
import ph.edu.cksc.college.appdev.appdev2025.common.composable.BasicToolbar
import ph.edu.cksc.college.appdev.appdev2025.common.composable.EmailField
import ph.edu.cksc.college.appdev.appdev2025.common.composable.PasswordField
import ph.edu.cksc.college.appdev.appdev2025.common.ext.basicButton
import ph.edu.cksc.college.appdev.appdev2025.common.ext.fieldModifier
import ph.edu.cksc.college.appdev.appdev2025.common.ext.textButton
import ph.edu.cksc.college.appdev.appdev2025.ui.theme.AppDev2025Theme
import ph.edu.cksc.college.appdev.appdev2025.R.string as AppText

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    fun signInUser() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
            .addOnFailureListener {
                // You can optionally add error handling here
            }
    }

    fun resetPassword() {
        if (email.isNotBlank()) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        }
    }

    LoginScreenContent(
        email = email,
        password = password,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onSignInClick = { signInUser() },
        onForgotPasswordClick = { resetPassword() }
    )
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    BasicToolbar(AppText.login_info)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(email, onEmailChange, Modifier.fieldModifier())
        PasswordField(password, onPasswordChange, Modifier.fieldModifier())

        BasicButton(AppText.sign_in, Modifier.basicButton()) { onSignInClick() }

        BasicTextButton(AppText.forget_password, Modifier.textButton()) {
            onForgotPasswordClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppDev2025Theme {
        LoginScreenContent(
            email = "email@test.com",
            password = "password123",
            onEmailChange = { },
            onPasswordChange = { },
            onSignInClick = { },
            onForgotPasswordClick = { }
        )
    }
}