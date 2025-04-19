package com.example.womensafety.ui.screens.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.womensafety.services.AccountService
import com.example.womensafety.services.FireStoreService
import com.example.womensafety.services.impl.FireStoreServiceImpl
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModelFactory(
    private val accountService: AccountService,
    private val context: Context,
    private val firestore: FireStoreService // Add Firestore as a parameter
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(accountService = accountService,context = context,
                firestore = firestore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
