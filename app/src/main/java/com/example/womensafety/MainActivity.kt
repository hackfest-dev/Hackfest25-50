package com.example.womensafety

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.womensafety.services.AccountService
import com.example.womensafety.services.impl.AccountServiceImpl
import com.example.womensafety.ui.theme.WomenSafetyTheme
import com.example.womensafety.utility.PermissionUtils
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class MainActivity : ComponentActivity() {
    private val accountService: AccountService = AccountServiceImpl()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        PermissionUtils.initialize(this)
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        setContent {
            WomenSafetyTheme {
                WomenSafetyApp(accountService)
            }
        }
    }
}