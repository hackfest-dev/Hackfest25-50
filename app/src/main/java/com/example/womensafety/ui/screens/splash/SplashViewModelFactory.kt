package com.example.womensafety.ui.screens.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.womensafety.services.AccountService

class SplashViewModelFactory(
    private val context: Context,
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(context, accountService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}