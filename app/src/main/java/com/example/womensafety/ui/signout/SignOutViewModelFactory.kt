package com.example.womensafety.ui.signout

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.womensafety.services.AccountService

class SignOutViewModelFactory(
    private val context: Context,
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignOutViewModel::class.java)) {
            return SignOutViewModel(context, accountService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}