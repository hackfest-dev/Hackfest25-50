package com.example.womensafety.ui.screens.parental

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.womensafety.data.AppDatabase
import com.example.womensafety.services.AccountService
import com.example.womensafety.services.FireStoreService

class ParentalControlViewModelFactory(
    private val context: Context,
    private val database: AppDatabase,
    private val fireStoreService: FireStoreService,
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParentalControlViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParentalControlViewModel(context, database, fireStoreService, accountService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}