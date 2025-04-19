package com.example.womensafety.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.womensafety.services.AccountService

class LogInViewModelFactory(
    private val accountService: AccountService,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogInViewModel::class.java)) {
            return LogInViewModel(accountService, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}