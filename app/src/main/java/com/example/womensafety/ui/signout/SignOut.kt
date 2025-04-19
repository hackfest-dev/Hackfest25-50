package com.example.womensafety.ui.signout

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.womensafety.LOG_IN
import com.example.womensafety.services.AccountService
import com.example.womensafety.services.impl.AccountServiceImpl

@Composable
fun SignOut(
    openAndPopUp: (String, String) -> Unit,
    accountService: AccountService = AccountServiceImpl(),
    viewModel: SignOutViewModel = viewModel(
        factory = SignOutViewModelFactory(LocalContext.current, accountService)
    )
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Sign Out") },
            text = { Text("Are you sure you want to sign out?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.signOut()
                    showDialog = false
                    openAndPopUp(LOG_IN, "home")
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}