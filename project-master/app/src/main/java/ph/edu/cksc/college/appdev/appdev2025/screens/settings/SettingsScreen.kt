package ph.edu.cksc.college.appdev.appdev2025.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import ph.edu.cksc.college.appdev.appdev2025.common.composable.BasicToolbar
import ph.edu.cksc.college.appdev.appdev2025.common.composable.DangerousCardEditor
import ph.edu.cksc.college.appdev.appdev2025.common.composable.DialogCancelButton
import ph.edu.cksc.college.appdev.appdev2025.common.composable.DialogConfirmButton
import ph.edu.cksc.college.appdev.appdev2025.common.composable.RegularCardEditor
import ph.edu.cksc.college.appdev.appdev2025.common.ext.card
import ph.edu.cksc.college.appdev.appdev2025.common.ext.spacer
import ph.edu.cksc.college.appdev.appdev2025.ui.theme.AppDev2025Theme
import ph.edu.cksc.college.appdev.appdev2025.R.drawable as AppIcon
import ph.edu.cksc.college.appdev.appdev2025.R.string as AppText

@Composable
fun SettingsScreen(navController: NavHostController) {
    val user = FirebaseAuth.getInstance().currentUser
    val isAnonymousAccount = user?.isAnonymous ?: true
    val userEmail = user?.email

    SettingsScreenContent(
        uiState = SettingsUiState(isAnonymousAccount),
        userId = userEmail,
        onLoginClick = {
            navController.navigate("login")
        },
        onSignUpClick = {
            navController.navigate("signup")
        },
        onSignOutClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("splash") {
                popUpTo("settings") { inclusive = true }
            }
        },
        onDeleteMyAccountClick = {
            user?.delete()?.addOnCompleteListener {
                navController.navigate("splash") {
                    popUpTo("settings") { inclusive = true }
                }
            }
        }
    )
}

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    userId: String?,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(AppText.settings)
        Spacer(modifier = Modifier.spacer())

        if (uiState.isAnonymousAccount) {
            Text("Not Logged in")
            RegularCardEditor(AppText.sign_in, AppIcon.ic_signin, "", Modifier.card()) {
                onLoginClick()
            }

            RegularCardEditor(AppText.generate_account, AppIcon.ic_generate_account, "", Modifier.card()) {
                onSignUpClick()
            }
        } else {
            userId?.let { id ->
                Text("Logged in as $id")
            }
            SignOutCard { onSignOutClick() }
            DeleteMyAccountCard { onDeleteMyAccountClick() }
        }
    }
}

@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(AppText.signing_out, AppIcon.ic_exit_account, "", Modifier.card()) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.sign_out_title)) },
            text = { Text(stringResource(AppText.sign_out_message)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.signing_out) {
                    signOut()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    DangerousCardEditor(
        AppText.delete_user_account,
        AppIcon.ic_delete_account,
        "",
        Modifier.card()
    ) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.delete_account_title)) },
            text = { Text(stringResource(AppText.delete_account_message)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.delete_user_account) {
                    deleteMyAccount()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val uiState = SettingsUiState(isAnonymousAccount = false)

    AppDev2025Theme {
        SettingsScreenContent(
            uiState = uiState,
            userId = "example_user_email@example.com",
            onLoginClick = { },
            onSignUpClick = { },
            onSignOutClick = { },
            onDeleteMyAccountClick = { }
        )
    }
}